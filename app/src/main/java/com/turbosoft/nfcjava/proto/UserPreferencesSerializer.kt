package com.turbosoft.nfcjava.proto

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.Int32Value.parseFrom
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.turbosoft.nfcjava.Organization
import java.io.InputStream
import java.io.OutputStream



object UserPreferencesSerializer : Serializer<Organization> {
    override val defaultValue: Organization = Organization.getDefaultInstance()

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun readFrom(input: InputStream): Organization {
        try {
            return Organization.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun writeTo(t: Organization, output: OutputStream) = t.writeTo(output)
}