package dodia.novshield.discordbot.tickets.database

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.transactions.transaction


object Tickets : IntIdTable("tickets") {
    val channelId = long("channel_id").uniqueIndex()
    val authorId = long("author_id")
    val ticketType = varchar("ticket_type", 50)
    val createdAt = datetime("created_at")
    val closedAt = datetime("closed_at").nullable()
    val status = varchar("status", 20).default("OPEN")
}


object TicketFields : IntIdTable("ticket_fields") {
    val ticketId = reference("ticket_id", Tickets, onDelete = org.jetbrains.exposed.sql.ReferenceOption.CASCADE)
    val fieldLabel = varchar("field_label", 256)
    val fieldValue = text("field_value")
}


fun initDatabase() {
    Database.connect("jdbc:sqlite:tickets.db", driver = "org.sqlite.JDBC")

    transaction {
        SchemaUtils.create(Tickets, TicketFields)
    }
}