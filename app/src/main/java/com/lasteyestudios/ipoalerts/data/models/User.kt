package com.lasteyestudios.ipoalerts.data.models

data class User(
    val id: String?,
    val basePlatform: com.lasteyestudios.ipoalerts.data.models.BasePlatform,
    val name: String?,
    val username: String?,
    val profile: com.lasteyestudios.ipoalerts.data.models.UserProfile,
    val metrics: com.lasteyestudios.ipoalerts.data.models.UserMetrics,
    val metadata: Map<String, Any>?
)
data class UserProfile(
    val verified: Boolean?,
    val pics: List<Map<String, String?>>,
    val category: String?,
    val description: String?,
    val externalUrl: String?
)
data class UserMetrics(
    val followers: String?,
    val following: String?,
    val likes: String?,
    val media: String?
)
/*export interface User<P extends BasePlatform> {
  id: string;
  platform: P["name"];
  name: string;
  username: string;
  profile: {
    verified?: boolean;
    pic?: Partial<Record<"normal" | "high" | "low", string>>;
    category?: string;
    description?: string;
    externalUrl?: string;
  }
  metrics: Partial<Record<"followers" | "following" | "likes" | "media", number>>;
  metadata: Record<string, any>;
}*/