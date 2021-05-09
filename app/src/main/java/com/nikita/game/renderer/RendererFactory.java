package com.nikita.game.renderer;


public class RendererFactory {
    public WaveformRenderer createSimpleWaveformRenderer( int foreground, int background) {
        return SimpleWaveformRenderer.newInstance(background, foreground);

    }
}
