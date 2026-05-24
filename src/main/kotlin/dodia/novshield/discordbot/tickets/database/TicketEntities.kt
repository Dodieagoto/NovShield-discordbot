package dodia.novshield.discordbot.tickets.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Ticket(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Ticket>(Tickets)

    var channelId by Tickets.channelId
    var authorId by Tickets.authorId
    var ticketType by Tickets.ticketType
    var createdAt by Tickets.createdAt
    var closedAt by Tickets.closedAt
    var status by Tickets.status

    val fields by TicketField referrersOn TicketFields.ticketId
}

class TicketField(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TicketField>(TicketFields)

    var ticket by Ticket referencedOn TicketFields.ticketId
    var fieldLabel by TicketFields.fieldLabel
    var fieldValue by TicketFields.fieldValue
}