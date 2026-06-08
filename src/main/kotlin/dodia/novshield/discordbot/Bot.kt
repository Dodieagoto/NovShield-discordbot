package dodia.novshield.discordbot

import dodia.novshield.discordbot.tickets.listeners.selectMenuListener
import dodia.novshield.discordbot.tickets.listeners.modalSubmitListener
import dodia.novshield.discordbot.tickets.listeners.buttonListener

import dodia.novshield.discordbot.tickets.database.initDatabase

import io.github.cdimascio.dotenv.Dotenv

import dodia.novshield.discordbot.tickets.commands.CommandRegister
import dev.minn.jda.ktx.jdabuilder.light
import dev.minn.jda.ktx.events.listener
import net.dv8tion.jda.api.events.session.ReadyEvent
import net.dv8tion.jda.api.requests.GatewayIntent
import dev.minn.jda.ktx.coroutines.await
import dev.minn.jda.ktx.interactions.components.MediaGalleryItem
import net.dv8tion.jda.api.components.container.Container
import java.awt.Color
import net.dv8tion.jda.api.components.textdisplay.TextDisplay
import net.dv8tion.jda.api.components.separator.Separator
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.components.actionrow.ActionRow
import net.dv8tion.jda.api.components.mediagallery.MediaGallery
import net.dv8tion.jda.api.components.selections.StringSelectMenu
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.utils.ChunkingFilter
import net.dv8tion.jda.api.utils.MemberCachePolicy

fun main() {

    val token = Dotenv.load().get("TOKEN") ?:
        throw IllegalStateException("TOKEN not started in .env")

    val guildId = Dotenv.load().get("GUILD_ID") ?:
        throw IllegalStateException("GUILD_ID not started in .env")

    val sendPanelCommand = Dotenv.load().get("SEND_COMMAND") ?:
        throw IllegalStateException("SEND_COMMAND not started in .env")


        initDatabase()
        println("DB initialized")

        val jda = light(token) {
            enableIntents(

                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.MESSAGE_CONTENT,
            )

            setMemberCachePolicy(MemberCachePolicy.ALL)


            setChunkingFilter(ChunkingFilter.ALL)
        }


        jda.listener<MessageReceivedEvent> { event ->

            val upContainer = Container.of(
                MediaGallery.of(
                    MediaGalleryItem(
                        "https://cdn.discordapp.com/attachments/1499428662137720943/1507802277123985428/Frame_1642.png?ex=6a148b4c&is=6a1339cc&hm=4ac1695459132d45515e9f8443cbfd90bc3ab0949e1f1e2fdacfae2a14c0cd16&"
                    )
                ),

                Separator.createDivider(Separator.Spacing.LARGE),
                TextDisplay.of(
                    "> ### :identification_card: МЕНЮ ПОДДЕРЖКИ\n" +
                            "В этом канале вы можете подать жалобу или задать вопрос к Администрации сервера." +
                            "\n \n> Наша команда проекта совместно с администрацией постарается вам помочь в наикратчайшие сроки! Не пингуйте администрацию, система тикетов будет **автоматически** напоминать об открытом тикете.\n"
                ),

                Separator.createDivider(Separator.Spacing.LARGE),
                TextDisplay.of("> :warning: Ответ на тикет может занимать до **24 часов**!")

            ).withAccentColor(Color(0xEA9545).rgb)

            val selMenu = StringSelectMenu.create("sel_menu")

                .setPlaceholder("Выберите категорию тикета")

                .addOption(
                    "Подать жалобу в суд",
                    "SelectMenu_Court",
                    "Подать жалобу в суд на игрока за нарушение правил",
                    Emoji.fromUnicode("⚖️")
                )

                .addOption(
                    "Подать жалобу на модератора",
                    "SelectMenu_ModerComplaint",
                    "Подать жалобу на модератора за превышение полномочий",
                    Emoji.fromUnicode("🛡️")
                )

                .addOption(
                    "Задать технический вопрос",
                    "SelectMenu_TechnicalQuestion",
                    "Задать вопрос по техническим проблемам, связанными с игрой",
                    Emoji.fromUnicode("🛠️")
                )

                .addOption(
                    "Задать вопрос к парламенту",
                    "SelectMenu_ParliamentQuestion",
                    "Задать вопрос к парламенту по поводу законов, предложений и т.п",
                    Emoji.fromUnicode("🏛️")
                )

                .addOption(
                    "Зарегистрировать патент",
                    "SelectMenu_Patent",
                    "Зарегистрировать патент на интелектуальную собственность",
                    Emoji.fromUnicode("📜")
                )

                .addOption(
                    "Регистрация территории",
                    "SelectMenu_LandRegistration",
                    "Отметка территории на онлайн карте",
                    Emoji.fromUnicode("🗺️")

                )

                .build()


            val downContainer = Container.of(
                ActionRow.of(selMenu)
            ).withAccentColor(Color(0xEA9545).rgb)

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
        jda.listener<ReadyEvent> {
            println("Bot is ready!")
            println("Commands registered!")

        }

        jda.awaitReady()

    val guild = jda.getGuildById(guildId) ?: run {
        return
    }

    selectMenuListener(jda)
    modalSubmitListener(jda, guild)
    buttonListener(jda)
    CommandRegister.registerCommands(jda)


    }