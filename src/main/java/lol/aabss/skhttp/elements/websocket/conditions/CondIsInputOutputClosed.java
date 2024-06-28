package lol.aabss.skhttp.elements.websocket.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.jetbrains.annotations.NotNull;

import java.net.http.WebSocket;

@Name("Is Input/Output Closed")
@Description("Returns true if either the input or output is closed.")
@Examples({
        "if output is input closed"
})
@Since("1.3")
public class CondIsInputOutputClosed extends PropertyCondition<WebSocket> {

    static {
        register(CondIsInputOutputClosed.class, "(output|:input) closed", "websockets");
    }

    private boolean input;

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        input = parseResult.hasTag("input");
        return super.init(exprs, matchedPattern, isDelayed, parseResult);
    }

    @Override
    public boolean check(WebSocket webSocket) {
        if (input){
            return webSocket.isInputClosed();
        }
        return webSocket.isOutputClosed();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return (input ? "input" : "output") +" closed";
    }
}
