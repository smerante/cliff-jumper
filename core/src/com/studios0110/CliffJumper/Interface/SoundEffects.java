package com.studios0110.CliffJumper.Interface;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.studios0110.CliffJumper.Splash.Splash;

public class SoundEffects {
    private Sound jump,running,crash,swoosh,point;
    boolean runPlaying,crashPlayed,deathPlaying;
    public Music music,birds,death;
    public SoundEffects()
    {
        music = Splash.manager.get("Sounds/music.mp3");
        music.setLooping(true);
        death = Splash.manager.get("Sounds/death.mp3");
        death.setLooping(true);
        birds = Splash.manager.get("Sounds/birds.mp3");
        birds.setLooping(true);
        jump = Splash.manager.get("Sounds/jump.mp3");
        running = Splash.manager.get("Sounds/running.mp3");
        crash = Splash.manager.get("Sounds/crash.mp3");
        swoosh = Splash.manager.get("Sounds/swoosh.mp3");
        point = Splash.manager.get("Sounds/point.mp3");
        runPlaying = false;
        crashPlayed = false;
        deathPlaying = false;
    }
    public void jump(){
        jump.play();
    }

    public void swoosh(){
        swoosh.play();
    }
    public void point(){
        point.play();
    }
    public void crash(){
        if(!crashPlayed) {
            crashPlayed = true;
            crash.play();
        }
    }
    public void reset(){
        runPlaying = false;
        crashPlayed = false;
        deathPlaying = false;
        deathPlaying = false;
        stopDeath();
        stopSounds();
    }
    public void resetCrash(){
        crashPlayed = false;
    }
    public void running(){
        if(!runPlaying) {
            runPlaying = true;
            running.loop();
        }
    }
    public void stopRunning(){
        runPlaying = false;
        running.stop();
    }
    public void endAll(){
        music.stop();
        death.stop();
        birds.stop();
        running.stop();
        crash.stop();
        swoosh.stop();
        point.stop();
    }
   public void playMusic(){
       if(music.isPlaying())
           return;
       music.setLooping(true);
       music.setVolume(0.3f);
       music.play();
   }
   private void stopMusic(){
       music.stop();
   }
    public void playDeath(){
        if(!deathPlaying){
            deathPlaying = true;
            death.setLooping(true);
            death.setVolume(0.3f);
            death.play();
        }
    }
    private void stopDeath(){
        death.stop();
    }
    public void playBirds(){
        if(birds.isPlaying())
            return;
        birds.setLooping(true);
        birds.setVolume(0.1f);
        birds.play();
    }
    private void stopBirds(){
        birds.stop();
    }
   public void stopSounds(){
       stopRunning();
       stopBirds();
       stopMusic();
   }
}
