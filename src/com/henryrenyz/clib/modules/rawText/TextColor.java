package com.henryrenyz.clib.modules.rawText;

import com.henryrenyz.clib.modules.exception.InvalidColorException;

import java.awt.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TextColor {

    public static final Map<String, TextColor> reg = new HashMap<>();

    //Vanilla
    public static final TextColor BLACK = new TextColor("black", "#000000", '0');
    public static final TextColor DARK_BLUE = new TextColor("dark_blue", "#0000aa", '1');
    public static final TextColor DARK_GREEN = new TextColor("dark_green", "#00aa00", '2');
    public static final TextColor DARK_AQUA = new TextColor("dark_aqua", "#00aaaa", '3');
    public static final TextColor DARK_RED = new TextColor("dark_red", "#aa0000", '4');
    public static final TextColor DARK_PURPLE = new TextColor("dark_purple", "#aa00aa", '5');
    public static final TextColor GOLD = new TextColor("gold", "#ffaa00", '6');
    public static final TextColor GRAY = new TextColor("gray", "#aaaaaa", '7');
    public static final TextColor DARK_GRAY = new TextColor("dark_gray", "#555555", '8');
    public static final TextColor BLUE = new TextColor("blue", "#5555ff", '9');
    public static final TextColor GREEN = new TextColor("green", "#55ff55", 'a');
    public static final TextColor AQUA = new TextColor("aqua", "#55ffff", 'b');
    public static final TextColor RED = new TextColor("red", "#ff5555", 'c');
    public static final TextColor LIGHT_PURPLE = new TextColor("light_purple", "#ff55ff", 'd');
    public static final TextColor YELLOW = new TextColor("yellow", "#ffff55", 'e');
    public static final TextColor WHITE = new TextColor("white", "#ffffff", 'f');

    //Format code
    public static final TextColor BOLD = new TextColor("bold", 'l');
    public static final TextColor STRIKETHROUGH = new TextColor("strikethrough", 'm');
    public static final TextColor UNDERLINE = new TextColor("underline", 'n');
    public static final TextColor ITALIC = new TextColor("italic", 'o');
    public static final TextColor OBFUSCATED = new TextColor("obfuscated", 'k');
    public static final TextColor RESET = new TextColor("reset", 'r');

    //Custom Preset
        //2020-12-1 Auto generated list from Wikipedia.
    public static final TextColor DIMGRAY = new TextColor("DimGray", "#696969");                                    //昏灰
    public static final TextColor DARKGRAY = new TextColor("DarkGray", "#A9A9A9");                                  //暗灰
    public static final TextColor SILVER = new TextColor("Silver", "#C0C0C0");                                      //银色
    public static final TextColor LIGHTGRAY = new TextColor("LightGray", "#D3D3D3");                                //亮灰色
    public static final TextColor GAINSBORO = new TextColor("Gainsboro", "#DCDCDC");                                //庚斯博罗灰
    public static final TextColor WHITESMOKE = new TextColor("WhiteSmoke", "#F5F5F5");                              //白烟色
    public static final TextColor SNOW = new TextColor("Snow", "#FFFAFA");                                          //雪色
    public static final TextColor IRONGRAY = new TextColor("IronGray", "#625B57");                                  //铁灰色
    public static final TextColor SANDBEIGE = new TextColor("SandBeige", "#E6C3C3");                                //沙棕
    public static final TextColor ROSYBROWN = new TextColor("RosyBrown", "#BC8F8F");                                //玫瑰褐
    public static final TextColor LIGHTCORAL = new TextColor("LightCoral", "#F08080");                              //亮珊瑚色
    public static final TextColor INDIANRED = new TextColor("IndianRed", "#CD5C5C");                                //印度红
    public static final TextColor BROWN = new TextColor("Brown", "#A52A2A");                                        //褐色
    public static final TextColor FIREBRICK = new TextColor("FireBrick", "#B22222");                                //砖红色
    public static final TextColor MAROON = new TextColor("Maroon", "#800000");                                      //栗色
    public static final TextColor DARKRED = new TextColor("DarkRed", "#8B0000");                                    //暗红
    public static final TextColor STRONGRED = new TextColor("StrongRed", "#E60000");                                //鲜红
    public static final TextColor PERSIMMON = new TextColor("Persimmon", "#FF4D40");                                //柿子橙
    public static final TextColor MISTYROSE = new TextColor("MistyRose", "#FFE4E1");                                //雾玫瑰色
    public static final TextColor SALMON = new TextColor("Salmon", "#FA8072");                                      //鲑红
    public static final TextColor SCARLET = new TextColor("Scarlet", "#FF2400");                                    //腥红
    public static final TextColor TOMATO = new TextColor("Tomato", "#FF6347");                                      //蕃茄红
    public static final TextColor DARKSALMON = new TextColor("DarkSalmon", "#E9967A");                              //暗鲑红
    public static final TextColor CORAL = new TextColor("Coral", "#FF7F50");                                        //珊瑚红
    public static final TextColor ORANGERED = new TextColor("OrangeRed", "#FF4500");                                //橙红
    public static final TextColor LIGHTSALMON = new TextColor("LightSalmon", "#FFA07A");                            //亮鲑红
    public static final TextColor VERMILION = new TextColor("Vermilion", "#FF4D00");                                //朱红
    public static final TextColor SIENNA = new TextColor("Sienna", "#A0522D");                                      //赭黄
    public static final TextColor TROPICALORANGE = new TextColor("TropicalOrange", "#FF8033");                      //热带橙
    public static final TextColor CAMEL = new TextColor("Camel", "#A16B47");                                        //驼色
    public static final TextColor APRICOT = new TextColor("Apricot", "#E69966");                                    //杏黄
    public static final TextColor COCONUTBROWN = new TextColor("CoconutBrown", "#4D1F00");                          //椰褐
    public static final TextColor SEASHELL = new TextColor("Seashell", "#FFF5EE");                                  //海贝色
    public static final TextColor SADDLEBROWN = new TextColor("SaddleBrown", "#8B4513");                            //鞍褐
    public static final TextColor CHOCOLATE = new TextColor("Chocolate", "#D2691E");                                //巧克力色
    public static final TextColor BURNTORANGE = new TextColor("BurntOrange", "#CC5500");                            //燃橙
    public static final TextColor SUNORANGE = new TextColor("SunOrange", "#FF7300");                                //阳橙
    public static final TextColor PEACHPUFF = new TextColor("PeachPuff", "#FFDAB9");                                //粉扑桃色
    public static final TextColor SANDBROWN = new TextColor("SandBrown", "#F4A460");                                //沙褐
    public static final TextColor BRONZE = new TextColor("Bronze", "#B87333");                                      //铜色
    public static final TextColor LINEN = new TextColor("Linen", "#FAF0E6");                                        //亚麻色
    public static final TextColor HONEYORANGE = new TextColor("HoneyOrange", "#FFB366");                            //蜜橙
    public static final TextColor PERU = new TextColor("Peru", "#CD853F");                                          //秘鲁色
    public static final TextColor SEPIA = new TextColor("Sepia", "#704214");                                        //乌贼墨色
    public static final TextColor OCHER = new TextColor("Ocher", "#CC7722");                                        //赭色
    public static final TextColor BISQUE = new TextColor("Bisque", "#FFE4C4");                                      //陶坯黄
    public static final TextColor TANGERINE = new TextColor("Tangerine", "#F28500");                                //橘色
    public static final TextColor DARKORANGE = new TextColor("DarkOrange", "#FF8C00");                              //暗橙
    public static final TextColor ANTIQUEWHITE = new TextColor("AntiqueWhite", "#FAEBD7");                          //古董白
    public static final TextColor TAN = new TextColor("Tan", "#D2B48C");                                            //日晒色
    public static final TextColor BURLYWOOD = new TextColor("BurlyWood", "#DEB887");                                //硬木色
    public static final TextColor BLANCHEDALMOND = new TextColor("BlanchedAlmond", "#FFEBCD");                      //杏仁白
    public static final TextColor NAVAJOWHITE = new TextColor("NavajoWhite", "#FFDEAD");                            //那瓦霍白
    public static final TextColor MARIGOLD = new TextColor("Marigold", "#FF9900");                                  //万寿菊黄
    public static final TextColor PAPAYAWHIP = new TextColor("PapayaWhip", "#FFEFD5");                              //蕃木瓜色
    public static final TextColor PALEOCRE = new TextColor("PaleOcre", "#CCB38C");                                  //灰土色
    public static final TextColor KHAKI = new TextColor("Khaki", "#996B1F");                                        //卡其色
    public static final TextColor MOCCASIN = new TextColor("Moccasin", "#FFE4B5");                                  //鹿皮鞋色
    public static final TextColor OLDLACE = new TextColor("OldLace", "#FDF5E6");                                    //旧蕾丝色
    public static final TextColor WHEAT = new TextColor("Wheat", "#F5DEB3");                                        //小麦色
    public static final TextColor PEACH = new TextColor("Peach", "#FFE5B4");                                        //桃色
    public static final TextColor ORANGE = new TextColor("Orange", "#FFA500");                                      //橙色
    public static final TextColor FLORALWHITE = new TextColor("FloralWhite", "#FFFAF0");                            //花卉白
    public static final TextColor GOLDENROD = new TextColor("Goldenrod", "#DAA520");                                //金菊色
    public static final TextColor DARKGOLDENROD = new TextColor("DarkGoldenrod", "#B8860B");                        //暗金菊色
    public static final TextColor COFFEE = new TextColor("Coffee", "#4D3900");                                      //咖啡色
    public static final TextColor JASMINE = new TextColor("Jasmine", "#E6C35C");                                    //茉莉黄
    public static final TextColor AMBER = new TextColor("Amber", "#FFBF00");                                        //琥珀色
    public static final TextColor CORNSILK = new TextColor("Cornsilk", "#FFF8DC");                                  //玉米丝色
    public static final TextColor CHROMEYELLOW = new TextColor("ChromeYellow", "#E6B800");                          //铬黄
    public static final TextColor GOLDEN = new TextColor("Golden", "#FFD700");                                      //金色
    public static final TextColor LEMONCHIFFON = new TextColor("LemonChiffon", "#FFFACD");                          //柠檬绸色
    public static final TextColor LIGHTKHAKI = new TextColor("LightKhaki", "#F0E68C");                              //亮卡其色
    public static final TextColor PALEGOLDENROD = new TextColor("PaleGoldenrod", "#EEE8AA");                        //灰金菊色
    public static final TextColor DARKKHAKI = new TextColor("DarkKhaki", "#BDB76B");                                //暗卡其色
    public static final TextColor MIMOSA = new TextColor("Mimosa", "#E6D933");                                      //含羞草黄
    public static final TextColor CREAM = new TextColor("Cream", "#FFFDD0");                                        //奶油色
    public static final TextColor IVORY = new TextColor("Ivory", "#FFFFF0");                                        //象牙色
    public static final TextColor BEIGE = new TextColor("Beige", "#F5F5DC");                                        //米色
    public static final TextColor LIGHTYELLOW = new TextColor("LightYellow", "#FFFFE0");                            //亮黄
    public static final TextColor LIGHTGOLDENRODYELLOW = new TextColor("LightGoldenrodYellow", "#FAFAD2");          //亮金菊黄
    public static final TextColor CHAMPAGNEYELLOW = new TextColor("ChampagneYellow", "#FFFF99");                    //香槟黄
    public static final TextColor MUSTARD = new TextColor("Mustard", "#CCCC4D");                                    //芥末黄
    public static final TextColor MOONYELLOW = new TextColor("MoonYellow", "#FFFF4D");                              //月黄
    public static final TextColor OLIVE = new TextColor("Olive", "#808000");                                        //橄榄色
    public static final TextColor CANARYYELLOW = new TextColor("CanaryYellow", "#FFEF00");                          //鲜黄
    public static final TextColor MOSSGREEN = new TextColor("MossGreen", "#697723");                                //苔藓绿
    public static final TextColor LIGHTLIME = new TextColor("LightLime", "#CCFF00");                                //亮柠檬绿
    public static final TextColor OLIVEDRAB = new TextColor("OliveDrab", "#6B8E23");                                //橄榄军服绿
    public static final TextColor YELLOWGREEN = new TextColor("YellowGreen", "#9ACD32");                            //黄绿
    public static final TextColor DARKOLIVEGREEN = new TextColor("DarkOliveGreen", "#556B2F");                      //暗橄榄绿
    public static final TextColor APPLEGREEN = new TextColor("AppleGreen", "#8CE600");                              //苹果绿
    public static final TextColor GREENYELLOW = new TextColor("GreenYellow", "#ADFF2F");                            //绿黄
    public static final TextColor GRASSGREEN = new TextColor("GrassGreen", "#99E64D");                              //草绿
    public static final TextColor LAWNGREEN = new TextColor("LawnGreen", "#7CFC00");                                //草坪绿
    public static final TextColor CHARTREUSE = new TextColor("Chartreuse", "#7FFF00");                              //查特酒绿
    public static final TextColor FOLIAGEGREEN = new TextColor("FoliageGreen", "#73B839");                          //叶绿
    public static final TextColor FRESHLEAVES = new TextColor("FreshLeaves", "#99FF4D");                            //嫩绿
    public static final TextColor BRIGHTGREEN = new TextColor("BrightGreen", "#66FF00");                            //明绿
    public static final TextColor COBALTGREEN = new TextColor("CobaltGreen", "#66FF59");                            //钴绿
    public static final TextColor HONEYDEW = new TextColor("Honeydew", "#F0FFF0");                                  //蜜瓜绿
    public static final TextColor DARKSEAGREEN = new TextColor("DarkSeaGreen", "#8FBC8F");                          //暗海绿
    public static final TextColor LIGHTGREEN = new TextColor("LightGreen", "#90EE90");                              //亮绿
    public static final TextColor PALEGREEN = new TextColor("PaleGreen", "#98FB98");                                //灰绿
    public static final TextColor IVYGREEN = new TextColor("IvyGreen", "#36BF36");                                  //常春藤绿
    public static final TextColor FORESTGREEN = new TextColor("ForestGreen", "#228B22");                            //森林绿
    public static final TextColor LIMEGREEN = new TextColor("LimeGreen", "#32CD32");                                //柠檬绿
    public static final TextColor DARKGREEN = new TextColor("DarkGreen", "#006400");                                //暗绿
    public static final TextColor LIME = new TextColor("Lime", "#00FF00");                                          //鲜绿色
    public static final TextColor MALACHITE = new TextColor("Malachite", "#22C32E");                                //孔雀石绿
    public static final TextColor MINT = new TextColor("Mint", "#16982B");                                          //薄荷绿
    public static final TextColor CELADON = new TextColor("Celadon", "#ACE1AF");                                    //青瓷绿
    public static final TextColor VERYLIGHTMALACHITEGREEN = new TextColor("VeryLightMalachiteGreen", "#73E68C");    //孔雀石绿
    public static final TextColor TURQUOISEGREEN = new TextColor("TurquoiseGreen", "#4DE680");                      //绿松石绿
    public static final TextColor VIRIDIAN = new TextColor("Viridian", "#127436");                                  //铬绿
    public static final TextColor HORIZONBLUE = new TextColor("HorizonBlue", "#B8DDC8");                            //苍色
    public static final TextColor SEAGREEN = new TextColor("SeaGreen", "#2E8B57");                                  //海绿
    public static final TextColor MEDIUMSEAGREEN = new TextColor("MediumSeaGreen", "#3CB371");                      //中海绿
    public static final TextColor MINTCREAM = new TextColor("MintCream", "#F5FFFA");                                //薄荷奶油色
    public static final TextColor SPRINGGREEN = new TextColor("SpringGreen", "#00FF80");                            //春绿
    public static final TextColor PEACOCKGREEN = new TextColor("PeacockGreen", "#00A15C");                          //孔雀绿
    public static final TextColor MEDIUMSPRINGGREEN = new TextColor("MediumSpringGreen", "#00FA9A");                //中春绿色
    public static final TextColor MEDIUMAQUAMARINE = new TextColor("MediumAquamarine", "#66CDAA");                  //中碧蓝色
    public static final TextColor AQUAMARINE = new TextColor("Aquamarine", "#7FFFD4");                              //碧蓝色
    public static final TextColor CYANBLUE = new TextColor("CyanBlue", "#0DBF8C");                                  //青蓝
    public static final TextColor AQUABLUE = new TextColor("AquaBlue", "#66FFE6");                                  //水蓝
    public static final TextColor TURQUOISEBLUE = new TextColor("TurquoiseBlue", "#00FFEF");                        //土耳其蓝
    public static final TextColor TURQUOISE = new TextColor("Turquoise", "#40E0D0");                                //绿松石色
    public static final TextColor LIGHTSEAGREEN = new TextColor("LightSeaGreen", "#20B2AA");                        //亮海绿
    public static final TextColor MEDIUMTURQUOISE = new TextColor("MediumTurquoise", "#48D1CC");                    //中绿松石色
    public static final TextColor LIGHTCYAN = new TextColor("LightCyan", "#E0FFFF");                                //亮青
    public static final TextColor BABYBLUE = new TextColor("BabyBlue", "#89CFF0");                                  //浅蓝
    public static final TextColor PALETURQUOISE = new TextColor("PaleTurquoise", "#AFEEEE");                        //灰绿松石色
    public static final TextColor DARKSLATEGRAY = new TextColor("DarkSlateGray", "#2F4F4F");                        //暗岩灰
    public static final TextColor TEAL = new TextColor("Teal", "#008080");                                          //凫绿
    public static final TextColor DARKCYAN = new TextColor("DarkCyan", "#008B8B");                                  //暗青
    public static final TextColor CYAN = new TextColor("Cyan", "#00FFFF");                                          //青色
    public static final TextColor DARKTURQUOISE = new TextColor("DarkTurquoise", "#00CED1");                        //暗绿松石色
    public static final TextColor CADETBLUE = new TextColor("CadetBlue", "#5F9EA0");                                //军服蓝
    public static final TextColor PEACOCKBLUE = new TextColor("PeacockBlue", "#00808C");                            //孔雀蓝
    public static final TextColor POWDERBLUE = new TextColor("PowderBlue", "#B0E0E6");                              //粉蓝
    public static final TextColor STRONGBLUE = new TextColor("StrongBlue", "#006374");                              //浓蓝
    public static final TextColor LIGHTBLUE = new TextColor("LightBlue", "#ADD8E6");                                //亮蓝
    public static final TextColor PALEBLUE = new TextColor("PaleBlue", "#D1EDF2");                                  //灰蓝
    public static final TextColor SAXEBLUE = new TextColor("SaxeBlue", "#4798B3");                                  //萨克斯蓝
    public static final TextColor DEEPSKYBLUE = new TextColor("DeepSkyBlue", "#00BFFF");                            //深天蓝
    public static final TextColor MARINEBLUE = new TextColor("MarineBlue", "#00477D");                              //水手蓝
    public static final TextColor PRUSSIANBLUE = new TextColor("Prussianblue", "#003153");                          //普鲁士蓝
    public static final TextColor ALICEBLUE = new TextColor("AliceBlue", "#F0F8FF");                                //爱丽丝蓝
    public static final TextColor DODGERBLUE = new TextColor("DodgerBlue", "#1E90FF");                              //道奇蓝
    public static final TextColor MINERALBLUE = new TextColor("MineralBlue", "#004D99");                            //矿蓝
    public static final TextColor AZURE = new TextColor("Azure", "#007FFF");                                        //湛蓝
    public static final TextColor WEDGWOODBLUE = new TextColor("WedgwoodBlue", "#5686BF");                          //韦奇伍德瓷蓝
    public static final TextColor LIGHTSTEELBLUE = new TextColor("LightSteelBlue", "#B0C4DE");                      //亮钢蓝
    public static final TextColor COBALTBLUE = new TextColor("CobaltBlue", "#0047AB");                              //钴蓝
    public static final TextColor PALEDENIM = new TextColor("PaleDenim", "#5E86C1");                                //灰丁宁蓝
    public static final TextColor SALVIABLUE = new TextColor("SalviaBlue", "#4D80E6");                              //鼠尾草蓝
    public static final TextColor DARKPOWDERBLUE = new TextColor("DarkPowderBlue", "#003399");                      //暗粉蓝
    public static final TextColor SAPPHIRE = new TextColor("Sapphire", "#082567");                                  //蓝宝石色
    public static final TextColor INTERNATIONALKLEINBLUE = new TextColor("InternationalKleinBlue", "#002FA7");      //国际奇连蓝
    public static final TextColor CERULEANBLUE = new TextColor("Ceruleanblue", "#2A52BE");                          //蔚蓝
    public static final TextColor ROYALBLUE = new TextColor("RoyalBlue", "#4169E1");                                //品蓝
    public static final TextColor DARKMINERALBLUE = new TextColor("DarkMineralBlue", "#24367D");                    //暗矿蓝
    public static final TextColor ULTRAMARINE = new TextColor("Ultramarine", "#0033FF");                            //极浓海蓝
    public static final TextColor LAPISLAZULI = new TextColor("LapisLazuli", "#26619C");                            //天青石蓝
    public static final TextColor GHOSTWHITE = new TextColor("GhostWhite", "#F8F8FF");                              //幽灵白
    public static final TextColor LAVENDERMIST = new TextColor("LavenderMist", "#E6E6FA");                          //薰衣草紫
    public static final TextColor LAVENDERBLUE = new TextColor("LavenderBlue", "#CCCCFF");                          //长春花色
    public static final TextColor PERIWINKLE = new TextColor("Periwinkle", "#C3CDE6");                              //长春花色
    public static final TextColor MIDNIGHTBLUE = new TextColor("MidnightBlue", "#191970");                          //午夜蓝
    public static final TextColor NAVYBLUE = new TextColor("NavyBlue", "#000080");                                  //藏青
    public static final TextColor DARKBLUE = new TextColor("DarkBlue", "#00008B");                                  //暗蓝
    public static final TextColor MEDIUMBLUE = new TextColor("MediumBlue", "#0000CD");                              //中蓝
    public static final TextColor GRAYISHPURPLE = new TextColor("GrayishPurple", "#8674A1");                        //浅灰紫红
    public static final TextColor INDIGO = new TextColor("Indigo", "#4B0080");                                      //靛色
    public static final TextColor DARKSLATEBLUE = new TextColor("DarkSlateBlue", "#483D8B");                        //暗岩蓝
    public static final TextColor STEELBLUE = new TextColor("SteelBlue", "#4682B4");                                //钢青色
    public static final TextColor EMERALD = new TextColor("Emerald", "#50C878");                                    //碧绿
    public static final TextColor PURPLE = new TextColor("Purple", "#6A0DAD");                                      //紫色
    public static final TextColor SLATEBLUE = new TextColor("SlateBlue", "#6A5ACD");                                //岩蓝
    public static final TextColor CORNFLOWERBLUE = new TextColor("CornflowerBlue", "#6495ED");                      //矢车菊蓝
    public static final TextColor MEDIUMSLATEBLUE = new TextColor("MediumSlateBlue", "#7B68EE");                    //中岩蓝
    public static final TextColor VIOLET = new TextColor("Violet", "#7F00FF");                                      //紫罗兰色
    public static final TextColor SLATEGRAY = new TextColor("SlateGray", "#708090");                                //岩灰
    public static final TextColor LIGHTSLATEGRAY = new TextColor("LightSlateGray", "#778899");                      //亮岩灰
    public static final TextColor BLUEVIOLET = new TextColor("BlueViolet", "#8A2BE2");                              //蓝紫
    public static final TextColor DARKMAGENTA = new TextColor("DarkMagenta", "#8B008B");                            //暗洋红
    public static final TextColor PLUM = new TextColor("Plum", "#8E4585");                                          //梅红色
    public static final TextColor SKYBLUE = new TextColor("SkyBlue", "#87CEEB");                                    //天空蓝
    public static final TextColor LIGHTSKYBLUE = new TextColor("LightSkyBlue", "#87CEFA");                          //浅天蓝
    public static final TextColor BURGUNDY = new TextColor("Burgundy", "#800020");                                  //勃艮第酒红
    public static final TextColor PATRIARCH = new TextColor("Patriarch", "#800080");                                //宗主教
    public static final TextColor MEDIUMPURPLE = new TextColor("MediumPurple", "#9370DB");                          //中紫红
    public static final TextColor DARKVIOLET = new TextColor("DarkViolet", "#9400D3");                              //暗紫
    public static final TextColor DARKORCHID = new TextColor("DarkOrchid", "#9932CC");                              //暗兰紫
    public static final TextColor AMETHYST = new TextColor("Amethyst", "#9966CC");                                  //紫水晶色
    public static final TextColor MEDIUMORCHID = new TextColor("MediumOrchid", "#BA55D3");                          //中兰紫
    public static final TextColor LAVENDER = new TextColor("Lavender", "#B57EDC");                                  //薰衣草紫
    public static final TextColor PANSY = new TextColor("Pansy", "#7400A1");                                        //三色堇紫
    public static final TextColor MALLOW = new TextColor("Mallow", "#D94DFF");                                      //锦葵紫
    public static final TextColor OPERAMAUVE = new TextColor("OperaMauve", "#B784A7");                              //优品紫红
    public static final TextColor PAILLILAC = new TextColor("PailLilac", "#E6CFE6");                                //淡紫丁香色
    public static final TextColor MINERALVIOLET = new TextColor("MineralViolet", "#A39DAE");                        //矿紫
    public static final TextColor LIGHTVIOLET = new TextColor("LightViolet", "#B09DB9");                            //亮紫
    public static final TextColor WISTERIA = new TextColor("Wisteria", "#C9A0DC");                                  //紫藤色
    public static final TextColor LILAC = new TextColor("Lilac", "#C8A2C8");                                        //紫丁香色
    public static final TextColor MEDIUMLAVENDERMAGENTA = new TextColor("MediumLavenderMagenta", "#DDA0DD");        //梅红色
    public static final TextColor LAVENDERMAGENTA = new TextColor("LavenderMagenta", "#EE82EE");                    //亮紫
    public static final TextColor HELIOTROPE = new TextColor("Heliotrope", "#DF73FF");                              //缬草紫
    public static final TextColor MAUVE = new TextColor("Mauve", "#E0B0FF");                                        //木槿紫
    public static final TextColor THISTLE = new TextColor("Thistle", "#D8BFD8");                                    //蓟紫
    public static final TextColor CLEMATIS = new TextColor("Clematis", "#CCA3CC");                                  //铁线莲紫
    public static final TextColor MAGENTA = new TextColor("Magenta", "#FF00FF");                                    //洋红
    public static final TextColor FUCHSIA = new TextColor("Fuchsia", "#F400A1");                                    //品红
    public static final TextColor ORCHID = new TextColor("Orchid", "#DA70D6");                                      //兰紫
    public static final TextColor PEARLPINK = new TextColor("PearlPink", "#FFB3E6");                                //浅珍珠红
    public static final TextColor OLDROSE = new TextColor("OldRose", "#C08081");                                    //陈玫红
    public static final TextColor ROSEPINK = new TextColor("RosePink", "#FF66CC");                                  //浅玫瑰红
    public static final TextColor MEDIUMVIOLETRED = new TextColor("MediumVioletRed", "#C71585");                    //中青紫红
    public static final TextColor MAGENTAROSE = new TextColor("MagentaRose", "#FF0DA6");                            //洋玫瑰红
    public static final TextColor ROSE = new TextColor("Rose", "#FF007F");                                          //玫瑰红
    public static final TextColor RUBY = new TextColor("Ruby", "#CC0080");                                          //红宝石色
    public static final TextColor CAMELLIA = new TextColor("Camellia", "#E63995");                                  //山茶红
    public static final TextColor DEEPPINK = new TextColor("DeepPink", "#FF1493");                                  //深粉红
    public static final TextColor FLAMINGO = new TextColor("Flamingo", "#E68AB8");                                  //火鹤红
    public static final TextColor CORALPINK = new TextColor("CoralPink", "#FF80BF");                                //浅珊瑚红
    public static final TextColor HOTPINK = new TextColor("HotPink", "#FF69B4");                                    //暖粉红
    public static final TextColor SPINELRED = new TextColor("SpinelRed", "#FF73B3");                                //尖晶石红
    public static final TextColor CARMINE = new TextColor("Carmine", "#E6005C");                                    //胭脂红
    public static final TextColor BABYPINK = new TextColor("BabyPink", "#FFD9E6");                                  //浅粉红
    public static final TextColor CARDINALRED = new TextColor("CardinalRed", "#990036");                            //枢机红
    public static final TextColor LAVENDERBLUSH = new TextColor("LavenderBlush", "#FFF0F5");                        //薰衣草紫红
    public static final TextColor PALEVIOLETRED = new TextColor("PaleVioletRed", "#DB7093");                        //灰紫红
    public static final TextColor CERISE = new TextColor("Cerise", "#DE3163");                                      //樱桃红
    public static final TextColor SALMONPINK = new TextColor("SalmonPink", "#FF8099");                              //浅鲑红
    public static final TextColor CRIMSON = new TextColor("Crimson", "#DC143C");                                    //绯红
    public static final TextColor PINK = new TextColor("Pink", "#FFC0CB");                                          //粉红
    public static final TextColor LIGHTPINK = new TextColor("LightPink", "#FFB6C1");                                //亮粉红
    public static final TextColor SHELLPINK = new TextColor("ShellPink", "#FFB3BF");                                //壳黄红
    public static final TextColor ALIZARINCRIMSON = new TextColor("AlizarinCrimson", "#E32636");                    //茜红

    private String code;
    private String hex;
    private char id = 'r';

    private int red;
    private int green;
    private int blue;

    public static final String colorCodePattern = "(§x(§[0-9a-fA-F]){6}|(§[0-9a-fA-FklnmorKLNMOR])+)";
    public static final Pattern colorCode = Pattern.compile("(§x(§[0-9a-fA-F]){6}|(§[0-9a-fA-FklnmorKLNMOR])+)");
    private static final Pattern pattern = Pattern.compile("^#([0-9a-fA-F]{6})$");
    private static final Pattern hexCode = Pattern.compile("§x(§[0-9a-fA-F]){6}");
    private static final Pattern normalCode = Pattern.compile("(§[0-9a-fA-FklnmorKLNMOR])+");

    public static final Map<String, TextColor> vanilla;
    public static final Map<String, TextColor> format;

    static {
        Map<String, TextColor> m1 = new HashMap<>();
        m1.put("BLACK", BLACK);
        m1.put("DARK_BLUE", DARK_BLUE);
        m1.put("DARK_GREEN", DARK_GREEN);
        m1.put("DARK_AQUA", DARK_AQUA);
        m1.put("DARK_RED", DARK_RED);
        m1.put("DARK_PURPLE", DARK_PURPLE);
        m1.put("GOLD", GOLD);
        m1.put("GRAY", GRAY);
        m1.put("DARK_GRAY", DARK_GRAY);
        m1.put("BLUE", BLUE);
        m1.put("GREEN", GREEN);
        m1.put("AQUA", AQUA);
        m1.put("RED", RED);
        m1.put("LIGHT_PURPLE", LIGHT_PURPLE);
        m1.put("YELLOW", YELLOW);
        m1.put("WHITE", WHITE);
        vanilla = Collections.unmodifiableMap(m1);

        Map<String, TextColor> m2 = new HashMap<>();
        m2.put("BOLD", BOLD);
        m2.put("ITALIC", ITALIC);
        m2.put("STRIKETHROUGH", STRIKETHROUGH);
        m2.put("UNDERLINE", UNDERLINE);
        m2.put("OBFUSCATED", OBFUSCATED);
        m2.put("RESET", RESET);
        format = Collections.unmodifiableMap(m2);
    }

    //Vanilla
    private TextColor(String code, String hex, char id) {
        this.code = code;
        this.id = id;
        this.hex = hex;
        Color c = Color.decode(hex);
        this.red = c.getRed();
        this.green = c.getGreen();
        this.blue = c.getBlue();
        reg.put(this.code.toUpperCase(), this);
    }

    //Format
    private TextColor(String code, char id) {
        this.code = code;
        this.id = id;
        this.hex = null;
        reg.put("", this);
    }

    //Custom preset
    public TextColor(String code, String hex) {
        this.code = code;
        this.hex =  hex;
        Color c = Color.decode(hex);
        this.red = c.getRed();
        this.green = c.getGreen();
        this.blue = c.getBlue();
        reg.put(this.code.toUpperCase(), this);
    }

    //Temp color
    public TextColor(String clr) {
        this.code = null;
        Matcher m = pattern.matcher(clr);
        if (m.find()) {
            this.hex = clr;
        } else {
            boolean b = false;
            for (TextColor c : vanilla.values()) {
                if (c.hex.equalsIgnoreCase(clr)) {
                    this.code = c.code;
                    this.hex = c.hex;
                    b = true;
                    break;
                }
            }
            if (!b) {
                for (TextColor c : reg.values()) {
                    if (c.code.equalsIgnoreCase(clr)) {
                        this.code = c.code;
                        this.hex = c.hex;
                        break;
                    }
                }
            }
        }
        if (this.hex != null) {
            Color c = Color.decode(hex);
            this.red = c.getRed();
            this.green = c.getGreen();
            this.blue = c.getBlue();
            return;
        }
        throw new InvalidColorException("No such registered color: " + clr);
    }

    //Convert color in json to String type, e.g #123456 --> §x§1§2§3§4§5§6
    public static String hexToColorCode(String jsonColor) {
        Matcher m = pattern.matcher(jsonColor);
        if (m.find()) {
            StringBuilder b = new StringBuilder("§x");
            for (char c : m.group(1).toCharArray()) {
                b.append("§").append(c);
            }
            return b.toString();
        } else {
            throw new InvalidColorException("No such color in Minecraft: " + jsonColor);
        }
    }

    public String hexToColorCode() {
        return hexToColorCode(this.hex);
    }

    public static String colorCodeToHex(String ori) {
        Matcher mt = hexCode.matcher(ori);
        if (mt.find()) {
            return '#' + ori.substring(3, 15).replaceAll("§", "");
        }
        throw new InvalidColorException("No such color: " + ori);
    }

    //Convert Hex to nearest vanilla color code
    public static String hexToVanilla(String hex) {
        Matcher m = pattern.matcher(hex);
        if (m.find()) {
            return nearestHEX(hex, vanilla);
        } else {
            for (TextColor t : vanilla.values()) {
                if (t.code.equals(hex)) return nearestHEX(t.hex, vanilla);
            }
        }
        throw new InvalidColorException("No such color in Minecraft: " + hex);
    }
    public String hexToVanilla() {
        return hexToVanilla(this.hex);
    }

    //Convert Hex to nearest REGISTERED color code(which contains vanilla and custom)
    public static String hexToNearest(String hex) {
        return nearestHEX(hex, reg);
    }
    public String hexToNearest() {
        return nearestHEX(this.hex, reg);
    }

    private static String nearestHEX(String hex, Map<String, TextColor> range) {
        Color c2 = Color.decode(hex);
        TextColor smallest = RESET;
        int     c,
                c1 = 2147483647;
        int     r = c2.getRed(),
                g = c2.getGreen(),
                b = c2.getBlue();

        for (TextColor clr : range.values()) {
            if (clr.hex != null && clr.id != 'r') {
                c = Math.abs(clr.red - r) + Math.abs(clr.green - g) + Math.abs(clr.blue - b);
                if (c < c1) {
                    smallest = clr;
                    c1 = c;
                }
                else c = c1;
            }
        }
        return smallest.getColorCode();
    }

    String getColorCode() {
        if (this.id != 'r') return '§' + Character.toString(this.id);
        else if (this.hex != null) return this.hexToColorCode();
        else return "r";
    }

    public String getCode() {
        return this.code;
    }

    public String getHex() {
        return hex;
    }

    public char getId() {
        return id;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public static TextColor[] values() {
        return reg.values().toArray(new TextColor[0]);
    }

    public static TextColor valueOf(String name) {
        if (reg.containsKey(name)) {
            return reg.get(name);
        }
        if (name == null)
            throw new NullPointerException("Name is null");
        throw new IllegalArgumentException(
                "No color constant " + name);
    }
}
