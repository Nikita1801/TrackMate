package com.nikita.game.renderer;


public class RendererFactory {
    public static WaveformRenderer createSimpleWaveformRenderer( int foreground, int background) {
        return SimpleWaveformRenderer.newInstance(background, foreground);

    }
}
