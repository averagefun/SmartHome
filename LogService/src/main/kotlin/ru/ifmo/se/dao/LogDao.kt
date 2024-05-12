package ru.ifmo.se.dao

import com.clickhouse.client.ClickHouseClient
import com.clickhouse.client.ClickHouseNodes
import com.clickhouse.client.ClickHouseProtocol
import com.clickhouse.data.ClickHouseDataStreamFactory
import com.clickhouse.data.ClickHouseFormat
import com.clickhouse.data.format.BinaryStreamUtils
import kotlinx.coroutines.future.await
import ru.ifmo.se.model.Log
import java.util.TimeZone

class LogDao(
    clickHouseUrl: String,
) {

    private val clickHouseClient: ClickHouseClient = ClickHouseClient.newInstance(ClickHouseProtocol.HTTP)
    private val clickHouseServer: ClickHouseNodes = ClickHouseNodes.of(clickHouseUrl)


    suspend fun saveLogEntity(entity: Log) {
        val request = clickHouseClient
            .read(clickHouseServer)
            .format(ClickHouseFormat.RowBinary)
            .write()
            .table("logs")

        val stream = ClickHouseDataStreamFactory.getInstance().createPipedOutputStream(request.config)
        BinaryStreamUtils.writeDateTime(stream, entity.timestamp.atZone(TimeZone.getTimeZone("Europe/Moscow").toZoneId()).toLocalDateTime(), TimeZone.getTimeZone("Europe/Moscow"))
        BinaryStreamUtils.writeString(stream, entity.thread)
        BinaryStreamUtils.writeString(stream, entity.level)
        BinaryStreamUtils.writeString(stream, entity.logger)
        BinaryStreamUtils.writeString(stream, entity.message)
        BinaryStreamUtils.writeString(stream, entity.environment)
        BinaryStreamUtils.writeString(stream, entity.service)

        stream.close()

        request.data(stream.inputStream)
            .execute()
            .await()
    }
}