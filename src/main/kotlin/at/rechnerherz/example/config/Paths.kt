package at.rechnerherz.example.config

const val ROOT_URL = "/"

const val API_URL = "/api"

const val PUBLIC_URL = "/public"
const val STATIC_URL = "$PUBLIC_URL/static"
const val CSP_REPORT_URL = "$PUBLIC_URL/csp-report"
const val DOCUMENTS_URL = "$PUBLIC_URL/documents"

const val LOGIN_URL = "$PUBLIC_URL/login"
const val LOGOUT_URL = "$PUBLIC_URL/logout"
const val AUTHENTICATED_ACCOUNT_URL = "$PUBLIC_URL/account"

const val FAVICON = "/favicon.ico"
const val SITEMAP = "/sitemap.xml"
const val ROBOTS = "/robots.txt"

const val ACTUATOR_URL = "/actuator"
const val JAVA_MELODY_URL = "/monitoring"

const val ANY_PATH = "/**"

fun anySubPath(path: String): String = "$path/**"

const val STATIC_DIRECTORY = "static/"
const val DOCUMENTS_DIRECTORY = "documents/"
