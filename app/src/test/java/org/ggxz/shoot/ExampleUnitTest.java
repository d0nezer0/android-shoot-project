package org.ggxz.shoot;

import org.ggxz.shoot.mvp.view.activity.MainActivity;
import org.junit.Test;

import static org.junit.Assert.*;

import android.content.Context;

import com.example.common_module.utils.player.AudioPlayerHelper;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        AudioPlayerHelper audioPlayerHelper=new AudioPlayerHelper();
        audioPlayerHelper.play("8.7","右上",false);
    }
}