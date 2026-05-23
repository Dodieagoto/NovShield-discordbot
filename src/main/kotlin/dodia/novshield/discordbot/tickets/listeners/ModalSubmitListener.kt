package dodia.novshield.discordbot.tickets.listeners


import dev.minn.jda.ktx.jdabuilder.light
import dev.minn.jda.ktx.events.listener
import net.dv8tion.jda.api.events.session.ReadyEvent
import net.dv8tion.jda.api.requests.GatewayIntent

import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData
import net.dv8tion.jda.api.interactions.commands.OptionType
import dev.minn.jda.ktx.coroutines.await
import net.dv8tion.jda.api.JDA

import dev.minn.jda.ktx.interactions.components.Modal

import net.dv8tion.jda.api.components.container.Container
import java.awt.Color

import dev.minn.jda.ktx.coroutines.await

import net.dv8tion.jda.api.components.textdisplay.TextDisplay
import net.dv8tion.jda.api.components.separator.Separator
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder
import net.dv8tion.jda.api.entities.Message.MentionType
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction


import dev.minn.jda.ktx.events.onCommand
import dev.minn.jda.ktx.coroutines.await
import dev.minn.jda.ktx.interactions.components.button
import net.dv8tion.jda.api.components.buttons.ButtonStyle

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.components.actionrow.ActionRow
import net.dv8tion.jda.api.components.buttons.Button
import net.dv8tion.jda.api.components.selections.SelectMenu
import net.dv8tion.jda.api.components.selections.StringSelectMenu
import java.awt.datatransfer.StringSelection
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent

import dodia.novshield.discordbot.tickets.panels.Court
import dodia.novshield.discordbot.tickets.panels.ModerComplaint
import dodia.novshield.discordbot.tickets.panels.ParliamentQuestion
import dodia.novshield.discordbot.tickets.panels.Patent
import dodia.novshield.discordbot.tickets.panels.TechnicalQuestion
import dodia.novshield.discordbot.tickets.panels.TerritoryReg


public fun modalSubmitListener(jda: JDA) {

    jda.listener<ModalInteractionEvent> { event ->

        when (event.modalId) {

            "Court_Panel" -> {
                Court.handleModalSubmit(event)
                event.deferEdit().queue()
                return@listener
            }

            "ModerComplaint_panel" -> {
                ModerComplaint.handleModalSubmit(event)
                event.deferEdit().queue()
                return@listener
            }

            "Parliament_panel" -> {
                ParliamentQuestion.handleModalSubmit(event)
                event.deferEdit().queue()
                return@listener
            }

            "Patent_panel" -> {
                Patent.handleModalSubmit(event)
                event.deferEdit().queue()
                return@listener
            }

            "TechnicalQuestion_panel" -> {
                TechnicalQuestion.handleModalSubmit(event)
                event.deferEdit().queue()
                return@listener
            }

            "Territory_panel" -> {
                TerritoryReg.handleModalSubmit(event)
                event.deferEdit().queue()
                return@listener

            }

        }
    }
}









