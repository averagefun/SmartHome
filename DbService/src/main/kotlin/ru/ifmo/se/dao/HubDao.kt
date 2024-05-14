package ru.ifmo.se.dao

import com.clickhouse.client.ClickHouseClient
import com.clickhouse.client.ClickHouseNodes
import com.clickhouse.client.ClickHouseProtocol
import com.clickhouse.data.ClickHouseFormat
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.await
import kotlinx.coroutines.launch
import ru.ifmo.se.model.AggregatedState
import ru.ifmo.se.model.HubState

class HubDao(
    clickHouseUrl: String,
) {
    private val clickHouseClient: ClickHouseClient = ClickHouseClient.newInstance(ClickHouseProtocol.HTTP)
    private val clickHouseServer: ClickHouseNodes = ClickHouseNodes.of(clickHouseUrl)

    @OptIn(DelicateCoroutinesApi::class)
    fun saveStateEntries(entity: List<HubState>) {
        var query = "insert into states (hub_id, id, value, date) VALUES"
        entity.forEach{hubState -> hubState.state.rooms.forEach{ room -> room.sensors.forEach { sensor ->
            query += "(${hubState.hubId}, ${sensor.id}, ${sensor.value}, now()),"

        } }}

        val request = clickHouseClient
            .read(clickHouseServer)
            .write()
            .format(ClickHouseFormat.RowBinary)
            .query(query)

        GlobalScope.launch {
            request.execute().await()
        }


    }

    fun getStates(hubId: Long, id: Long, from: String?, to: String?) : List<AggregatedState> {
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