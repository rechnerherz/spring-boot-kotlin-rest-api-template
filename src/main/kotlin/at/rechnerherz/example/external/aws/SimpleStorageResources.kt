package at.rechnerherz.example.external.aws

import org.springframework.cloud.aws.core.io.s3.SimpleStorageResource

fun SimpleStorageResource.bucketName(): String =
    uri.path.removePrefix("/").substringBefore("/")

fun SimpleStorageResource.objectName(): String =
    uri.path.removePrefix("/").substringAfter("/")
