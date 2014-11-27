/**
 * This file is part of WorldInfo, licensed under the MIT License (MIT).
 *
 * Copyright (c) WorldInfo
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package tk.coaster3000.worldinfo.common.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property.Type;
import tk.coaster3000.worldinfo.WorldInfoMod;
import tk.coaster3000.worldinfo.common.config.properties.BaseProperty;
import tk.coaster3000.worldinfo.common.data.MultiMap;
import tk.coaster3000.worldinfo.util.PropertyTypes;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ForgeSettings extends BasicSettings<HashMap<String, Object>> {

	static final Map<Class<?>, Type> clsToType;

	static {
		clsToType = new HashMap<Class<?>, Type>();

		clsToType.put(String.class, Type.STRING);
		clsToType.put(Double.class, Type.DOUBLE);
		clsToType.put(Float.class, Type.DOUBLE);
		clsToType.put(Integer.class, Type.INTEGER);
		clsToType.put(Short.class, Type.INTEGER);
		clsToType.put(Long.class, Type.INTEGER);
		clsToType.put(Mode.class, Type.STRING);
		clsToType.put(Boolean.class, Type.BOOLEAN);
	}

	private static final String PATH_SEP = ".";
	private final File configFile;
	private final Configuration config;

	public ForgeSettings(File configFile) {
		super(new MultiMap<HashMap<String, Object>>() {
			@Override
			protected HashMap<String, Object> craftDataHolder() {
				return new HashMap<String, Object>();
			}
		});

		this.config = new Configuration(configFile);
		this.configFile = configFile;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void save() {
		for (Property<?> rawProperty : getProperties()) {
			final String cat;
			final String key;
			if (rawProperty instanceof BaseProperty) {
				cat = ((BaseProperty) rawProperty).getPath();
				key = rawProperty.getKey();
			} else {
				final String t = rawProperty.getKey();
				key = t.substring(t.lastIndexOf(PATH_SEP) + 1);
				cat = t.substring(0, t.lastIndexOf(PATH_SEP));
			}

			Type t = null;
			if (clsToType.containsKey(rawProperty.getValueType())) t = clsToType.get(rawProperty.getValueType());
			else {
				WorldInfoMod.logger.error(String.format("Could not find a type match for '%s'!", rawProperty.getClass().getName()));
				continue;
			}

			net.minecraftforge.common.config.Property prop = new net.minecraftforge.common.config.Property(key, (String) null, t);

			//Handle comments
			if (rawProperty.getComment() != null) prop.comment = rawProperty.getComment();

			//Handle regex validator
			if (rawProperty instanceof PropertyTypes.StringProperty || t == Type.STRING) {
				if (rawProperty instanceof PatternValidatedProperty) prop.setValidationPattern(((PatternValidatedProperty) rawProperty).getPattern());
				prop.set(((Property<String>)rawProperty).getValue(this));
			} else if (rawProperty instanceof PropertyTypes.NumberProperty) {

				PropertyTypes.NumberProperty<?> p = (PropertyTypes.NumberProperty<?>) rawProperty;
				switch (t) {
					case DOUBLE:
						if (rawProperty instanceof RangedProperty)
							prop.setMinValue(
									((RangedProperty<Number>) rawProperty).getMinValue().doubleValue()
							).setMaxValue(
									((RangedProperty<Number>) rawProperty).getMinValue().doubleValue()
							);
						prop.set(p.getValue().doubleValue());
						break;
					case INTEGER:
						if (rawProperty instanceof RangedProperty)
							prop.setMinValue(
									((RangedProperty<Number>) rawProperty).getMinValue().intValue())
									.setMaxValue(
											((RangedProperty<Number>) rawProperty).getMinValue().intValue());
						prop.set(p.getValue().intValue());
						break;
					default:
						WorldInfoMod.logger.error(String.format(
								"The property was not handled correctly. "
										+ "The property is a number property and "
										+ "the forge property type was set to '%s'!",
								t.name()));
						break;
				}
			} else if (t == Type.BOOLEAN) {
				prop.set(((Property<Boolean>)rawProperty).getValue(this));
			} else {
				WorldInfoMod.logger.error("ForgeSettings Handler:Config saving system could not find a valid save method. "
						+ "Contact the developer about this error as soon as possible!");
				continue;
			}

			config.getCategory(cat).put(key, prop);
		}
		config.save();
	}

	@Override
	@SuppressWarnings("unchecked")
	public void load() {
		config.load();
		data.clear();

		for (Property<?> rawProperty : getProperties()) {
			final String cat;
			final String key;

			if (rawProperty instanceof BaseProperty) {
				cat = ((BaseProperty) rawProperty).getPath();
				key = rawProperty.getKey();
			} else {
				final String t = rawProperty.getKey();
				key = t.substring(t.lastIndexOf(PATH_SEP) + 1);
				cat = t.substring(0, t.lastIndexOf(PATH_SEP));
			}
			final String node = cat + "." + key;

			Type t = clsToType.get(rawProperty.getValueType());

			if (t == null) {
				WorldInfoMod.logger.error(String.format("Could not find a type match for '%s'!", rawProperty.getClass().getName()));
				continue;
			}

			switch (t) {
				case STRING:
					if (rawProperty instanceof PatternValidatedProperty)
						data.set(node, config.getString(
							key,
							cat,
							((Property<String>)rawProperty).getDefaultValue(),
							rawProperty.getComment(),
							((PatternValidatedProperty)rawProperty).getPattern()));
					else data.set(node, config.getString(key, cat, ((Property<String>)rawProperty).getDefaultValue(), rawProperty.getComment()));
					break;
				case INTEGER:
					if (rawProperty.getClass().equals(Short.class))
						data.set(node, (short) (config.get(
								key,
								cat,
								((Property<Short>) rawProperty).getDefaultValue(),
								rawProperty.getComment()).getInt()));
					else if (rawProperty.getClass().equals(Long.class))
						data.set(node, (long)(config.get(
								key,
								cat,
								((Property<Long>)rawProperty).getDefaultValue(),
								rawProperty.getComment()).getInt()));
					else
						data.set(node, config.get(
								key,
								cat,
								((Property<Integer>)rawProperty).getDefaultValue(),
								rawProperty.getComment()).getInt());
					break;
				case DOUBLE:
					if (rawProperty.getClass().equals(Float.class))
						data.set(node, (float) (config.get(
								key,
								cat,
								((Property<Float>) rawProperty).getDefaultValue(),
								rawProperty.getComment()).getDouble()));
					else
						data.set(node, config.get(
								key,
								cat,
								((Property<Double>) rawProperty).getDefaultValue(),
								rawProperty.getComment()).getDouble());

					break;
				case BOOLEAN:
				data.set(node, config.getBoolean(
							key,
							cat,
							((Property<Boolean>) rawProperty).getDefaultValue(),
							rawProperty.getComment()));
				break;
//				case COLOR:
//					break;
//				case MOD_ID:
//					break;
				default:

					break;
			}
		}
	}

	@Override
	public boolean supportsComments() {
		return true;
	}

	public File getConfigFile() {
		return configFile;
	}
}
