package com.xmartlabs.template.service

import com.xmartlabs.template.model.Project

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface RepositoryService {
  companion object {
    const val BASE_URL = "repos"
    const val OWNER = "owner"
    const val REPO = "repo"
    const val GET_PROJECTS = "$BASE_URL/{$OWNER}/{$REPO}/projects"
  }

  @GET(GET_PROJECTS)
  fun getAllProjects(@Path(OWNER) owner: String, @Path(REPO) repo: String): Single<List<Project>>
}
