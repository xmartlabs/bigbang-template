package com.xmartlabs.template.model

import com.xmartlabs.bigbang.core.model.SessionType

class Session(override var accessToken: String? = null, internal var user: User? = null) : SessionType
