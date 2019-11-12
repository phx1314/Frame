package com.mdx.framework.service.exception

import java.io.IOException

class HttpResultException(val code: Int, val msg: String) : IOException()