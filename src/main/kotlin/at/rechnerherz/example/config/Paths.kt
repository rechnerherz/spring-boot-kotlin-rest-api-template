package at.rechnerherz.example.config

const val ROOT_URL = "/"

const val API_URL = "/api"

const val PUBLIC_URL = "/public"
const val STATIC_URL = "/public/static"
const val CSP_REPORT_URL = "/public/csp-report"

const val LOGIN_URL = "/public/login"
const val LOGOUT_URL = "/public/logout"
const val AUTHENTICATED_ACCOUNT_URL = "/public/account"

const val FAVICON = "/favicon.ico"
const val SITEMAP = "/sitemap.xml"
const val ROBOTS = "/robots.txt"

const val ACTUATOR_URL = "/actuator"
const val JAVA_MELODY_URL = "/monitoring"

const val ANY_PATH = "/**"

fun anySubPath(path: String): String = "$path/**"

const val STATIC_DIRECTORY = "static/"
const val DOCUMENTS_DIRECTORY = "documents/"
