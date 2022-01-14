package com.lasteyestudios.ipoalerts.data.models

data class Media(
    val id: String?,
    val basePlatform: com.lasteyestudios.ipoalerts.data.models.BasePlatform,
    val contentType: String?,
    val user: com.lasteyestudios.ipoalerts.data.models.User,
    val images: List<com.lasteyestudios.ipoalerts.data.models.MediaCandidate>?,  //url,height,weight
    val videos: List<com.lasteyestudios.ipoalerts.data.models.MediaCandidate>?,  //url,height,weight
    val audio: com.lasteyestudios.ipoalerts.data.models.MediaAudio?,
    val description: String?,
    val metrics: com.lasteyestudios.ipoalerts.data.models.MediaMetrics
)
//val s = Media(id = "sgs",basePlatform = )
/*
export interface Media<P extends BasePlatform> {
  id: string;
  platform: P["name"];
  type: P["mediaTypes"];
  contentType: "image" | "video";
  user: User<P>;
  images?: MediaCandidate[];
  videos?: MediaCandidate[];
  audio?: MediaAudio;
  description?: string;
  metrics: Partial<Record<"likes" | "views" | "comments", number>>;
}
 */
