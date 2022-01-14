package com.lasteyestudios.ipoalerts.data.models

data class MediaResponse(
    val media: List<com.lasteyestudios.ipoalerts.data.models.Media>,
    val pagination: com.lasteyestudios.ipoalerts.data.models.Pagination?,
)
data class Pagination(
    val id: String,
    val hasMore: Boolean
)
/*
export interface MediaResponse<P extends BasePlatform> {
  media: Media<P>[];
  pagination?: {
    id: string;
    hasMore?: boolean;
  }
}
 */