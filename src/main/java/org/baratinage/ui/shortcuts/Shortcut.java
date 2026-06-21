package org.baratinage.ui.shortcuts;

import java.awt.event.KeyEvent;
import java.util.StringJoiner;

import javax.swing.KeyStroke;

import org.json.JSONObject;

public record Shortcut(int keyCode, int modifiers) {
  public static Shortcut of(int keyCode, int modifiers) {
    return new Shortcut(keyCode, modifiers);
  }

  public static Shortcut of(String key, boolean ctrl, boolean shift, boolean alt) {
    int keyCode = KeyEvent.getExtendedKeyCodeForChar(key.toUpperCase().charAt(0));
    if (keyCode == KeyEvent.VK_UNDEFINED)
      throw new IllegalArgumentException("Unknown key: " + key);

    int modifiers = 0;
    if (ctrl)
      modifiers |= KeyEvent.CTRL_DOWN_MASK;
    if (shift)
      modifiers |= KeyEvent.SHIFT_DOWN_MASK;
    if (alt)
      modifiers |= KeyEvent.ALT_DOWN_MASK;

    return new Shortcut(keyCode, modifiers);
  }

  public static Shortcut of(JSONObject json) {
    String key = json.optString("key", "");
    boolean ctrl = json.optBoolean("ctrl", false);
    boolean shift = json.optBoolean("shift", false);
    boolean alt = json.optBoolean("alt", false);
    return of(key, ctrl, shift, alt);
  }

  KeyStroke getKeyStroke() {
    return KeyStroke.getKeyStroke(keyCode, modifiers);
  }

  boolean ctrl() {
    return (modifiers & KeyEvent.CTRL_DOWN_MASK) != 0;
  }

  boolean shift() {
    return (modifiers & KeyEvent.SHIFT_DOWN_MASK) != 0;
  }

  boolean alt() {
    return (modifiers & KeyEvent.ALT_DOWN_MASK) != 0;
  }

  String keyText() {
    return KeyEvent.getKeyText(keyCode);
  }

  String toDisplayString() {
    var parts = new StringJoiner("+");
    if (ctrl())
      parts.add("Ctrl");
    if (shift())
      parts.add("Shift");
    if (alt())
      parts.add("Alt");
    parts.add(keyText());
    return parts.toString();
  }

  JSONObject toJSON() {
    JSONObject json = new JSONObject();
    // saving in a humane readable format
    json.put("key", keyText());
    json.put("ctrl", ctrl());
    json.put("shift", shift());
    json.put("alt", alt());
    return json;
  }
}
