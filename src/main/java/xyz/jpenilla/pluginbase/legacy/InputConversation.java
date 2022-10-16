package xyz.jpenilla.pluginbase.legacy;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import org.bukkit.conversations.BooleanPrompt;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class InputConversation {
    private BiConsumer<Player, String> acceptedListener;
    private BiConsumer<Player, String> deniedListener;
    private BiPredicate<Player, String> inputValidator;
    private Function<Player, String> promptHandler;
    private BiFunction<Player, String, String> confirmText;
    private final PluginBase pluginBase;
    private boolean localEcho = false;

    @Deprecated
    public InputConversation() {
        this(PluginBase.instance());
    }

    private InputConversation(final PluginBase pluginBase) {
        this.pluginBase = pluginBase;
    }

    public InputConversation onAccepted(BiConsumer<Player, String> acceptedListener) {
        this.acceptedListener = acceptedListener;
        return this;
    }

    public InputConversation onDenied(BiConsumer<Player, String> deniedListener) {
        this.deniedListener = deniedListener;
        return this;
    }

    public InputConversation onValidateInput(BiPredicate<Player, String> inputValidator) {
        this.inputValidator = inputValidator;
        return this;
    }

    public InputConversation onPromptText(Function<Player, String> promptHandler) {
        this.promptHandler = promptHandler;
        return this;
    }

    public InputConversation onConfirmText(BiFunction<Player, String, String> confirmTextHandler) {
        this.confirmText = confirmTextHandler;
        return this;
    }

    public InputConversation localEcho(final boolean localEcho) {
        this.localEcho = localEcho;
        return this;
    }

    public void start(final Player player) {
        final Conversation conversation = this.pluginBase.conversationFactory()
                .withFirstPrompt(new StringPrompt() {
                    @Override
                    public @NonNull String getPromptText(@NonNull ConversationContext conversationContext) {
                        return promptHandler.apply((Player) conversationContext.getForWhom());
                    }

                    @Override
                    public Prompt acceptInput(@NonNull ConversationContext conversationContext, @Nullable String s) {
                        if (!inputValidator.test((Player) conversationContext.getForWhom(), s)) {
                            return this;
                        }
                        if (acceptedListener != null || deniedListener != null) {
                            return new BooleanPrompt() {
                                @Override
                                protected @Nullable Prompt acceptValidatedInput(@NonNull ConversationContext conversationContext, boolean b) {
                                    if (b && acceptedListener != null) {
                                        acceptedListener.accept((Player) conversationContext.getForWhom(), s);
                                    } else if (deniedListener != null) {
                                        deniedListener.accept((Player) conversationContext.getForWhom(), s);
                                    }
                                    return null;
                                }

                                @Override
                                public @NonNull String getPromptText(@NonNull ConversationContext conversationContext) {
                                    return confirmText.apply((Player) conversationContext.getForWhom(), s);
                                }
                            };
                        }
                        return null;
                    }
                })
                .withLocalEcho(this.localEcho)
                .buildConversation(player);
        conversation.begin();
    }

    public static InputConversation create() {
        return new InputConversation(PluginBase.instance());
    }
}
