package com.lasteyestudios.ipoalerts.data.models

data class MediaAudio(
    val id: String?,
    val artist: String?,
    val title: String?,
    val artworkUrl: String?,
    val media: String?,
    val metadata: Map<String, String?>
)
/*
export interface MediaAudio {
  id: string
  artist?: string;
  title?: string;
  artworkUrl?: string;
  metrics: {
    "media": number
  }
  metadata: Record<string, any>
  // type: "original_sounds" | "licensed_music";
  // pkType: "audio_cluster_id" | "music_canonical_id";
}
 */