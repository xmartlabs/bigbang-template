package com.xmartlabs.template.model

enum class AffiliationType {
  COLLABORATOR,
  ORGANIZATION_MEMBER,
  OWNER;

  override fun toString(): String {
    return name.toLowerCase()
  }
}
