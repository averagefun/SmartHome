package ru.ifmo.se.dao

import com.clickhouse.client.ClickHouseClient
import com.clickhouse.client.ClickHouseNodes
import com.clickhouse.client.ClickHouseParameterizedQuery
import com.clickhouse.client.ClickHouseProtocol
import com.clickhouse.data.ClickHouseDataStreamFactory
import com.clickhouse.data.ClickHouseFormat
import com.clickhouse.data.format.BinaryStreamUtils
import kotlinx.coroutines.future.await
import ru.ifmo.se.logger
import ru.ifmo.se.model.AggregatedState
import ru.ifmo.se.model.HubState
import java.time.LocalDateTime
import java.util.TimeZone

class HubDao(
    clickHouseUrl: String,
) {
    private val clickHouseClient: ClickHouseClient = ClickHouseClient.newInstance(ClickHouseProtocol.HTTP)
    private val clickHouseServer: ClickHouseNodes = ClickHouseNodes.of(clickHouseUrl)


    suspend fun saveStateEntries(entity: HubState) {
        val time = LocalDateTime.now()
        entity.state.rooms.forEach{ room -> room.sensors.forEach { sensor ->
            val request = clickHouseClient
                .read(clickHouseServer)
                .format(ClickHouseFormat.RowBinary)
                .write()
                .table("states")

            val stream = ClickHouseDataStreamFactory.getInstance().createPipedOutputStream(request.config)
            BinaryStreamUtils.writeInt64(stream, entity.hubId)
            BinaryStreamUtils.writeInt64(stream, sensor.id)
            BinaryStreamUtils.writeFloat64(stream, sensor.value)
            BinaryStreamUtils.writeDateTime(stream, time, TimeZone.getDefault())

            stream.close()

            request.data(stream.inputStream)
                .execute()
                .await()
        } }
    }

    suspend fun getStates(hubId: Long, id: Long, from: String?, to: String?) : List<AggregatedState> {
        var states = emptyList<AggregatedState>()
        ClickHouseClient.newInstance(ClickHouseProtocol.HTTP).use { client ->
            client.read(clickHouseServer)
                .format(ClickHouseFormat.RowBinaryWithNamesAndTypes)
                .query("select * from states where hub_id=:hub_id and id=:id and date>=:from and date<=:to")
                .params(hubId.toString(), id.toString(), "'$from'", "'$to'")
                .executeAndWait().use { response ->
                    for (r in response.records()) {
                        states = states.plus(AggregatedState(
                            r.getValue(0).asLong(),
                            r.getValue(1).asLong(),
                            r.getValue(2).asDouble(),
                            r.getValue(3).asDateTime()
                        ))
                    }
                }
        }
        return states
    }
}