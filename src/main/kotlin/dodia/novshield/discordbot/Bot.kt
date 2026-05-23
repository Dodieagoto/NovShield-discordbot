package dodia.novshield.discordbot

import dodia.novshield.discordbot.tickets.listeners.selectMenuListener
import dodia.novshield.discordbot.tickets.listeners.modalSubmitListener

import io.github.cdimascio.dotenv.Dotenv


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




fun main(){

    val token = Dotenv.load().get("TOKEN") ?:
        throw IllegalStateException("TOKEN not started in .env")

    val guildId = Dotenv.load().get("GUILD_ID") ?:
        throw IllegalStateException("GUILD_ID not started in .env")

    val sendPanelCommand = Dotenv.load().get("SEND_COMMAND") ?:
        throw IllegalStateException("SEND_COMMAND not started in .env")



    val jda = light(token){
        enableIntents(

            GatewayIntent.GUILD_MESSAGES,
            GatewayIntent.GUILD_MEMBERS,
            GatewayIntent.MESSAGE_CONTENT,
        )
    }

    jda.listener<MessageReceivedEvent> { event ->

        val upContainer = Container.of(
            TextDisplay.of("Тест сообщения с конпонентами"),
            Separator.createDivider(Separator.Spacing.LARGE),
            TextDisplay.of("гойда"),

        ).withAccentColor(Color(0xFF0000).rgb)

        val selMenu = StringSelectMenu.create("sel_menu")

            .setPlaceholder("Выберите категорию тикета")

            .addOption(
                "Подать жалобу в суд",
                "SelectMenu_Court",
                "Подать жалобу в суд на игрока за нарушение правил",
                Emoji.fromUnicode("⚖️"))

            .addOption(
                "Подать жалобу на модератора",
                "SelectMenu_ModerComplaint",
                "Подать жалобу на модератора за превышение полномочий",
                Emoji.fromUnicode("🛡️"))

            .addOption(
                "Задать технический вопрос",
                "SelectMenu_TechnicalQuestion",
                "Задать вопрос по техническим проблемам, связанными с игрой",
                Emoji.fromUnicode("🛠️"))

            .addOption(
                "Задать вопрос к парламенту",
                "SelectMenu_ParliamentQuestion",
                "Задать вопрос к парламенту по поводу законов, предложений и т.п",
                Emoji.fromUnicode("🏛️"))


            .addOption(
                "Зарегистрировать патент",
                "SelectMenu_Patent",
                "Зарегистрировать патент на интелектуальную собственность",
                Emoji.fromUnicode("📜"))

            .addOption(
                "Регистрация территории",
                "SelectMenu_LandRegistration",
                "Отметка территории на онлайн карте",
                Emoji.fromUnicode("🗺️")

            )

            .build()


        val downContainer = Container.of(
            ActionRow.of(selMenu)
        ).withAccentColor(Color(0xFF0000).rgb)

        val message = MessageCreateBuilder()
            .useComponentsV2()
            .setComponents(upContainer, downContainer)
            .build()

        if (event.author.isBot) return@listener
        if (event.message.contentRaw == sendPanelCommand) {

            event.channel.sendMessage(message).await()

            event.message.delete().await()
        }
    }


    selectMenuListener(jda)
    modalSubmitListener(jda)

    jda.listener<ReadyEvent> {
        println("Bot is ready!")
        println("Commands registered!")

    }

    jda.awaitReady()

}