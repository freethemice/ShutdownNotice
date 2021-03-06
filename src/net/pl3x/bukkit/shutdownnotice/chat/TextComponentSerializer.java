package net.pl3x.bukkit.shutdownnotice.chat;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.List;
import net.pl3x.bukkit.shutdownnotice.api.chat.BaseComponent;
import net.pl3x.bukkit.shutdownnotice.api.chat.TextComponent;

public class TextComponentSerializer
  extends BaseComponentSerializer
  implements JsonSerializer<TextComponent>, JsonDeserializer<TextComponent>
{
  public TextComponent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
    throws JsonParseException
  {
    TextComponent component = new TextComponent();
    JsonObject object = json.getAsJsonObject();
    deserialize(object, component, context);
    component.setText(object.get("text").getAsString());
    return component;
  }
  

  public JsonElement serialize(TextComponent src, Type typeOfSrc, JsonSerializationContext context)
  {
    List<BaseComponent> extra = src.getExtra();
    JsonObject object = new JsonObject();
    if ((src.hasFormatting()) || ((extra != null) && (!extra.isEmpty())))
    {
      serialize(object, src, context);
    }
    object.addProperty("text", src.getText());
    return object;
  }
}
