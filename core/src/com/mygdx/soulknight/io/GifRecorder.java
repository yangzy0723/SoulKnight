package com.mygdx.soulknight.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.soulknight.assets.Variable;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;

/** Records and saves GIFs. */
public class GifRecorder{
    private RecorderController controller;
    private Matrix4 matrix;
    private TextureRegion region;
    private Batch batch;
    private Array<byte[]> frames;
    private float gif_x;
    private float gif_y;
    private float gifWidth;
    private float gifHeight;
    private FileHandle exportdirectory, workdirectory;
    private float frameTime;
    private boolean recording;
    private boolean saving;
    private float saveProgress;
    public GifRecorder(Batch batch) {
        this(batch, Gdx.files.local("data/gif_export"), Gdx.files.local(".gifimages"));
    }

    public GifRecorder(Batch batch, FileHandle exportDirectory, FileHandle workDirectory) {
        this.batch = batch;
        gif_x = Variable.WINDOWS_SIZE * -0.5f;;
        gif_y = Variable.WINDOWS_SIZE * -0.5f;
        gifWidth = Variable.WINDOWS_SIZE;
        gifHeight = Variable.WINDOWS_SIZE;
        this.workdirectory = workDirectory;
        this.exportdirectory = exportDirectory;

        Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();

        controller = new DefaultController();
        matrix = new Matrix4();
        region = new TextureRegion(new Texture(pixmap));
        frames = new Array<>();

        pixmap.dispose();
    }
    /** Updates the recorder and draws the GUI */
    public void update() {

        doInput();

        matrix.set(batch.getProjectionMatrix());
        batch.getProjectionMatrix().setToOrtho2D(0, 0, Variable.WINDOWS_SIZE, Variable.WINDOWS_SIZE);

        batch.begin();
        if (recording)
            batch.setColor(Color.RED);
        else
            batch.setColor(Color.GREEN);
        batch.draw(region, 0, 0, gifWidth, 1f);
        batch.draw(region, 0, gifHeight - 1, gifWidth, 1f);
        batch.draw(region, 0, 0, 1f, gifHeight);
        batch.draw(region, gifWidth - 1, 0, 1f, gifHeight);

        if (saving) {
            batch.setColor(Color.BLACK);

            float w = 200, h =25;
            batch.draw(region, 50, h / 2 + 15, w, h);

            //this just blends red and green
            Color a = Color.RED;
            Color b = Color.GREEN;

            float s = saveProgress;
            float i = 1f - saveProgress;

            batch.setColor(a.r * i + b.r * s, a.g * i + b.g * s, a.b * i + b.b * s, 1f);

            batch.draw(region, 50, h / 2 + 15, w * saveProgress, h);
        }
        if (recording) {
            frameTime += Gdx.graphics.getDeltaTime() * 61f * Variable.SPEED_MULTIPLIER;
            if (frameTime >= ((float) 60 / Variable.RECORD_FPS)) {
                byte[] pix = ScreenUtils.getFrameBufferPixels((int) (gif_x) + 1 + Variable.WINDOWS_SIZE / 2,
                        (int) (gif_y) + 1 + Variable.WINDOWS_SIZE / 2,
                        (int) (gifWidth) - 2, (int) (gifHeight) - 2, true);
                frames.add(pix);
                frameTime = 0;
            }
        }

        batch.setColor(Color.WHITE);
        batch.end();
        batch.getProjectionMatrix().set(matrix);
    }
    public void endRecord(){
        if(!saving && recording){
            finishRecording();
            writeGIF(workdirectory, exportdirectory);
        }
    }
    private void doInput(){
        if(controller.recordKeyPressed() && !saving){
            if(!recording)
                startRecording();
            else{
                finishRecording();
                writeGIF(workdirectory, exportdirectory);
            }
        }
    }
    private void startRecording(){
        clearFrames();
        recording = true;
    }
    private void finishRecording(){
        recording = false;
    }

   private void clearFrames(){
        frames.clear();
        recording = false;
    }
    private void writeGIF(final FileHandle directory, final FileHandle writedirectory){
        if(saving)
            return;
        saving = true;
        final Array<String> strings = new Array<>();
        final Array<Pixmap> pixmaps = new Array<>();

        for(byte[] bytes : frames){
            Pixmap pixmap = createPixmap(bytes);
            pixmaps.add(pixmap);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {

                saveProgress = 0;
                int i = 0;
                for (Pixmap pixmap : pixmaps) {
                    PixmapIO.writePNG(Gdx.files.absolute(directory.file().getAbsolutePath() + "/frame" + i + ".png"), pixmap);
                    strings.add("frame" + i + ".png");
                    saveProgress += (0.5f / pixmaps.size);
                    i++;
                }

                GifRecorder.this.compileGIF(strings, directory, writedirectory);
                directory.deleteDirectory();
                for (Pixmap pixmap : pixmaps) {
                    pixmap.dispose();
                }
                saving = false;
            }
        }).start();
    }
    private File compileGIF(Array<String> strings, FileHandle inputdirectory, FileHandle directory){
        if(strings.size == 0){
            throw new RuntimeException("No strings!");
        }

        try{
            String time = "" + (int) (System.currentTimeMillis() / 1000);
            String dirstring = inputdirectory.file().getAbsolutePath();
            new File(directory.file().getAbsolutePath()).mkdir();
            BufferedImage firstImage = ImageIO.read(new File(dirstring + "/" + strings.get(0)));
            File file = new File(directory.file().getAbsolutePath() + "/recording" + time + ".gif");
            ImageOutputStream output = new FileImageOutputStream(file);
            GifSequenceWriter writer = new GifSequenceWriter(output, firstImage.getType(), (int) (1f / Variable.RECORD_FPS * 1000f), true);

            writer.writeToSequence(firstImage);

            for(int i = 1; i < strings.size; i++){
                BufferedImage after = ImageIO.read(new File(dirstring + "/" + strings.get(i)));
                saveProgress += (0.5f / frames.size);
                writer.writeToSequence(after);
            }
            writer.close();
            output.close();
            return file;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    private Pixmap createPixmap(byte[] pixels, int width, int height){
        Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
        BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);

        Color color = new Color();
        for(int x = 0; x < pixmap.getWidth(); x++) {
            for (int y = 0; y < pixmap.getHeight(); y++) {
                color.set(pixmap.getPixel(x, y));
                if (color.a <= 0.999f) {
                    color.a = 1f;
                    pixmap.setColor(color);
                    pixmap.drawPixel(x, y);
                }
            }
        }
        return pixmap;
    }
    private Pixmap createPixmap(byte[] pixels){
        return createPixmap(pixels, (int) (gifWidth) - 2, (int) (gifHeight) - 2);
    }
    /** Default controller implementation, uses the provided keys */
    static class DefaultController implements RecorderController{
        public boolean recordKeyPressed(){
            return Gdx.input.isKeyJustPressed(Variable.RECORD_KEY);
        }
    }
}
