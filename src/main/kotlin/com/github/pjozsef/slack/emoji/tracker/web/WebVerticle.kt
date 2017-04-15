package com.github.pjozsef.slack.emoji.tracker.web

import io.vertx.core.AbstractVerticle
import io.vertx.core.http.HttpServerOptions
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import io.vertx.core.net.JksOptions
import io.vertx.core.net.PemKeyCertOptions
import sun.security.tools.keytool.CertAndKeyGen
import sun.security.x509.X500Name
import java.io.FileOutputStream
import java.security.KeyStore
import java.util.*


class WebVerticle : AbstractVerticle() {

    val log = LoggerFactory.getLogger(this::class.java)

    override fun start() {
        vertx.createHttpServer(createServerOptions(config()))
                .requestHandler { WebRouter.create(vertx, config()).accept(it) }
                .listen(config().getInteger("port"))
    }

    private fun createServerOptions(config: JsonObject) = HttpServerOptions().apply {
        isSsl = true
        if (config.containsKey("ssl")) {
            withPemCert(config.getJsonObject("ssl"))
        } else {
            withSelfSigned()
        }
    }

    private fun HttpServerOptions.withPemCert(config: JsonObject) {
        log.info("Loading pem certificates.")
        val pemOptions = PemKeyCertOptions().apply {
            certPath = config.getString("certificate")
            keyPath = config.getString("key")
        }
        pemKeyCertOptions = pemOptions
    }

    private fun HttpServerOptions.withSelfSigned() {
        log.info("No certificate config found, generating self signed certificate.")
        val store = KeyStore.getInstance("JKS")
        store.load(null, null)
        val keypair = CertAndKeyGen("RSA", "SHA1WithRSA", null)
        val x500Name = X500Name("localhost", "IT", "unknown", "unknown", "unknown", "unknown")
        keypair.generate(1024)

        val privKey = keypair.privateKey
        val chain = arrayOf(keypair.getSelfCertificate(x500Name, Date(), 365 * 24 * 60 * 60))

        val password = UUID.randomUUID().toString()

        store.setKeyEntry("selfsigned", privKey, password.toCharArray(), chain)
        store.store(FileOutputStream(".keystore"), password.toCharArray())
        keyStoreOptions = JksOptions().setPath(".keystore").setPassword(password)
    }
}