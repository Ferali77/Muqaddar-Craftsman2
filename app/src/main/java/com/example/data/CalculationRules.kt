package com.example.data

import java.util.Locale

data class ExtraTaskResult(
    val title: String,
    val unit: String,
    val unitPrice: Double,
    val quantity: Double,
    val total: Double
)

data class AdaptedCalculationResult(
    val difficultyMultiplier: Double,
    val materialMultiplier: Double,
    val extraTasks: List<ExtraTaskResult>,
    val explanationLines: List<String>
)

object CalculationRules {

    fun calculateAdaptedRules(
        type: String,
        params: Map<String, Float>,
        rooms: List<RoomEntity>,
        openingsMap: Map<Long, List<OpeningEntity>>,
        baseSubtotal: Double
    ): AdaptedCalculationResult {
        var difficultyMultiplier = 1.0
        var materialMultiplier = 1.0
        val extraTasks = mutableListOf<ExtraTaskResult>()
        val explanationLines = mutableListOf<String>()

        // Get baseline spec difficulty
        val spec = ProjectTypeDetails.getSpecForType(type)
        difficultyMultiplier = spec.baseDifficultyMultiplier
        materialMultiplier = spec.defaultMaterialCostFactor

        when (type) {
            "شقة" -> {
                val hasElevator = params["has_elevator"] ?: 1.0f
                val finishLevel = params["finish_level"] ?: 2.0f
                
                if (hasElevator == 0.0f) {
                    difficultyMultiplier += 0.08
                    explanationLines.add("• تم تطبيق زيادة صعوبة (+8%) لعدم توفر مصعد في الشقة (صعوبة نقل العتاد).")
                }
                if (finishLevel == 1.0f) {
                    difficultyMultiplier -= 0.10
                    explanationLines.add("• فينيسيون عادي: خصم (-10%) من تكلفة اليد العاملة.")
                } else if (finishLevel == 3.0f) {
                    difficultyMultiplier += 0.15
                    explanationLines.add("• فينيسيون لوكس: زيادة (+15%) لتسوية الجدران وتدقيق المعجون.")
                }
            }
            "منزل" -> {
                val floorCount = params["floor_count"] ?: 1.0f
                val hasFacade = params["has_facade"] ?: 0.0f
                
                if (floorCount > 1f) {
                    val factor = (floorCount - 1) * 0.05
                    difficultyMultiplier += factor
                    explanationLines.add("• زيادة صعوبة الارتفاع (+${(factor * 100).toInt()}%): لتعدد الطوابق ($floorCount طوابق).")
                }
                if (hasFacade == 1.0f) {
                    val facadeArea = rooms.sumOf { (it.length + it.width) * 2 } * 3.5 // rough estimation
                    if (facadeArea > 0) {
                        extraTasks.add(
                            ExtraTaskResult(
                                title = "دهان الواجهة الخارجية للمنزل (تقديري)",
                                unit = "متر مربع",
                                unitPrice = 350.0,
                                quantity = facadeArea,
                                total = facadeArea * 350.0
                            )
                        )
                        explanationLines.add("• إدراج تقديري لأعمال الواجهة الخارجية بمساحة ${String.format(Locale.ENGLISH, "%.1f", facadeArea)} م².")
                    }
                }
            }
            "فيلا" -> {
                val complexity = params["gypsum_complexity"] ?: 2.0f
                val hasDecor = params["has_decorative_paint"] ?: 1.0f
                
                if (complexity == 3.0f) {
                    difficultyMultiplier += 0.15
                    explanationLines.add("• جبس بورد فاخر جداً: زيادة (+15%) للعمل الدقيق والمحيط المعقد.")
                }
                if (hasDecor == 1.0f) {
                    materialMultiplier += 0.15
                    explanationLines.add("• تفعيل الدهانات الديكورية: زيادة في معامل استهلاك المواد (+15%).")
                }
            }
            "محل" -> {
                val hasEpoxy = params["commercial_epoxy"] ?: 0.0f
                if (hasEpoxy == 1.0f) {
                    val floorArea = rooms.sumOf { if (it.hasFlooring) it.length * it.width else 0.0 }
                    if (floorArea > 0) {
                        extraTasks.add(
                            ExtraTaskResult(
                                title = "تأسيس وطلاء أرضية إيبوكسي تجاري مانع للاحتكاك",
                                unit = "متر مربع",
                                unitPrice = 1200.0,
                                quantity = floorArea,
                                total = floorArea * 1200.0
                            )
                        )
                        explanationLines.add("• إضافة طلاء الإيبوكسي التجاري للأرضيات بمساحة ${String.format(Locale.ENGLISH, "%.1f", floorArea)} م².")
                    }
                }
            }
            "عيادة" -> {
                val antibac = params["antibacterial_paint"] ?: 1.0f
                val epoxy = params["seamless_epoxy_floor"] ?: 1.0f
                
                if (antibac == 1.0f) {
                    materialMultiplier += 0.20
                    explanationLines.add("• دهان طبي مضاد للبكتيريا: زيادة استهلاك المواد (+20%) لتوفير طلاءات صحية معتمدة.")
                }
                if (epoxy == 1.0f) {
                    val floorArea = rooms.sumOf { if (it.hasFlooring) it.length * it.width else 0.0 }
                    if (floorArea > 0) {
                        extraTasks.add(
                            ExtraTaskResult(
                                title = "تركيب أرضية إيبوكسي طبية مستمرة مضادة للجراثيم",
                                unit = "متر مربع",
                                unitPrice = 1800.0,
                                quantity = floorArea,
                                total = floorArea * 1800.0
                            )
                        )
                        explanationLines.add("• إدراج طلاء الإيبوكسي الطبي المستمر لـ ${String.format(Locale.ENGLISH, "%.1f", floorArea)} م² من الأرضيات.")
                    }
                }
            }
            "مدرسة" -> {
                val guardHeight = params["wall_guard_height"] ?: 1.2f
                val discount = params["large_volume_discount"] ?: 1.0f
                
                val perimeter = rooms.sumOf { (it.length + it.width) * 2 }
                if (perimeter > 0) {
                    val guardArea = perimeter * guardHeight
                    extraTasks.add(
                        ExtraTaskResult(
                            title = "طلاء الجزء السفلي بدهان زيتي مضاد للصدمات (مصد حماية)",
                            unit = "متر مربع",
                            unitPrice = 280.0,
                            quantity = guardArea,
                            total = guardArea * 280.0
                        )
                    )
                    explanationLines.add("• طلاء حماية سفلي بارتفاع $guardHeight م وبمساحة إجمالية ${String.format(Locale.ENGLISH, "%.1f", guardArea)} م².")
                }
                if (discount == 1.0f && baseSubtotal > 150000) {
                    difficultyMultiplier -= 0.10
                    explanationLines.add("• خصم كميات كبير للمشروعات التعليمية (-10%) على المصنعية.")
                }
            }
            "مطعم" -> {
                val fireRetardant = params["kitchen_fire_retardant"] ?: 1.0f
                val loungeDecor = params["lounge_decorative_walls"] ?: 20.0f
                
                if (fireRetardant == 1.0f) {
                    materialMultiplier += 0.10
                    explanationLines.add("• طلاء مقاوم للحريق بالكامل في المطابخ (+10% تكلفة المواد الافتراضية).")
                }
                if (loungeDecor > 0) {
                    extraTasks.add(
                        ExtraTaskResult(
                            title = "تشطيبات طلاء ديكوري فاخر لجدران صالة المطعم",
                            unit = "متر مربع",
                            unitPrice = 450.0,
                            quantity = loungeDecor.toDouble(),
                            total = loungeDecor.toDouble() * 450.0
                        )
                    )
                    explanationLines.add("• إضافة دهان ديكوري راقٍ لـ $loungeDecor م² في صالة الطعام.")
                }
            }
            "مقهى" -> {
                val exposedCeiling = params["exposed_ceiling_paint"] ?: 0.0f
                val chalkboard = params["chalkboard_wall_area"] ?: 5.0f
                
                if (exposedCeiling == 1.0f) {
                    difficultyMultiplier += 0.12
                    explanationLines.add("• طلاء سقف صناعي مكشوف (أنابيب): زيادة صعوبة (+12%) لصعوبة الرش والدقة.")
                }
                if (chalkboard > 0) {
                    extraTasks.add(
                        ExtraTaskResult(
                            title = "طلاء جدار سبورة أسود طباشيري فني",
                            unit = "متر مربع",
                            unitPrice = 500.0,
                            quantity = chalkboard.toDouble(),
                            total = chalkboard.toDouble() * 500.0
                        )
                    )
                    explanationLines.add("• طلاء جدار سبورة فنية بمساحة $chalkboard م².")
                }
            }
            "مستودع" -> {
                val height = params["scaffolding_height"] ?: 6.0f
                val floorCoating = params["industrial_floor_coating"] ?: 1.0f
                
                if (height > 4.5f) {
                    val rentMultiplier = (height - 4.5f) * 0.05
                    difficultyMultiplier += rentMultiplier
                    explanationLines.add("• إيجار ونصب سقالات للعمل على ارتفاع $height م (+${(rentMultiplier * 100).toInt()}% صعوبة).")
                }
                if (floorCoating == 1.0f) {
                    val floorArea = rooms.sumOf { if (it.hasFlooring) it.length * it.width else 0.0 }
                    if (floorArea > 0) {
                        extraTasks.add(
                            ExtraTaskResult(
                                title = "طلاء أرضيات خرسانية إيبوكسي صناعي فائق التحمل",
                                unit = "متر مربع",
                                unitPrice = 1500.0,
                                quantity = floorArea,
                                total = floorArea * 1500.0
                            )
                        )
                        explanationLines.add("• إضافة طلاء الإيبوكسي الصناعي الثقيل للأرضيات بمساحة ${String.format(Locale.ENGLISH, "%.1f", floorArea)} م².")
                    }
                }
            }
            "ورشة" -> {
                val linesLength = params["safety_lines_length"] ?: 15f
                val sealer = params["oil_resistant_sealer"] ?: 1.0f
                
                if (linesLength > 0) {
                    extraTasks.add(
                        ExtraTaskResult(
                            title = "تخطيط وطلاء خطوط الأمان والتحذير الصفراء/السوداء للأرضية",
                            unit = "متر طولي",
                            unitPrice = 180.0,
                            quantity = linesLength.toDouble(),
                            total = linesLength.toDouble() * 180.0
                        )
                    )
                    explanationLines.add("• رسم وتخطيط خطوط الأمان الأرضية بطول $linesLength م.")
                }
                if (sealer == 1.0f) {
                    materialMultiplier += 0.10
                    explanationLines.add("• طلاء أساس عازل ومقاوم للرطوبة والزيوت لحماية الخرسانة (+10% مواد).")
                }
            }
            "مصنع" -> {
                val chemLevel = params["chemical_resistance_level"] ?: 2f
                val heatPaint = params["has_heat_paint"] ?: 0.0f
                
                if (chemLevel > 1f) {
                    val chemFactor = (chemLevel - 1f) * 0.15
                    materialMultiplier += chemFactor
                    explanationLines.add("• معالجة كيميائية مستوى $chemLevel: زيادة استهلاك ومواصفات المواد (+${(chemFactor * 100).toInt()}%).")
                }
                if (heatPaint == 1.0f) {
                    val wallArea = rooms.sumOf { (it.length + it.width) * 2 * it.height }
                    if (wallArea > 0) {
                        extraTasks.add(
                            ExtraTaskResult(
                                title = "تطبيق طلاءات حرارية متخصصة لحماية الجدران الساخنة",
                                unit = "متر مربع",
                                unitPrice = 800.0,
                                quantity = wallArea * 0.2, // estimated 20% of walls
                                total = (wallArea * 0.2) * 800.0
                            )
                        )
                        explanationLines.add("• طلاءات حرارية مخصصة لـ ${String.format(Locale.ENGLISH, "%.1f", wallArea * 0.2)} م² من الحوائط الصناعية.")
                    }
                }
            }
            "فندق" -> {
                val repCount = params["room_replication_count"] ?: 10f
                val wallpaper = params["has_wallpaper"] ?: 0.0f
                
                if (repCount > 3f) {
                    val repDiscount = minOf(0.20, (repCount - 3) * 0.01)
                    difficultyMultiplier -= repDiscount
                    explanationLines.add("• خصم تكرار الغرف المتطابقة (-${String.format(Locale.ENGLISH, "%.0f", repDiscount * 100)}%): لتسهيل العمل وتكرار التصميم لـ $repCount غرفة.")
                }
                if (wallpaper == 1.0f) {
                    val wallpaperArea = rooms.sumOf { (it.length + it.width) * 2 * it.height } * 0.3 // estimate 30% wallpaper
                    if (wallpaperArea > 0) {
                        extraTasks.add(
                            ExtraTaskResult(
                                title = "تركيب ورق حائط فاخر ومقاوم للرطوبة في الغرف والممرات",
                                unit = "متر مربع",
                                unitPrice = 600.0,
                                quantity = wallpaperArea,
                                total = wallpaperArea * 600.0
                            )
                        )
                        explanationLines.add("• تركيب ورق حائط فاخر لـ ${String.format(Locale.ENGLISH, "%.1f", wallpaperArea)} م².")
                    }
                }
            }
            "قاعة حفلات" -> {
                val ceilingHeight = params["ceiling_height"] ?: 5.0f
                val luxuryFactor = params["luxury_decor_factor"] ?: 2f
                
                if (ceilingHeight > 4.5f) {
                    val heightFactor = (ceilingHeight - 4.5f) * 0.08
                    difficultyMultiplier += heightFactor
                    explanationLines.add("• معامل صعوبة السقف المرتفع جداً بالقاعة $ceilingHeight م (+${(heightFactor * 100).toInt()}%).")
                }
                if (luxuryFactor == 3f) {
                    difficultyMultiplier += 0.20
                    explanationLines.add("• زخارف جبسية بمستوى ملكي معقد: زيادة (+20%) على صعوبة المصنعية.")
                }
            }
            "مسجد" -> {
                val domeRadius = params["dome_radius"] ?: 0f
                val calligraphyLength = params["calligraphy_length"] ?: 10f
                
                if (domeRadius > 0) {
                    val domeArea = 2 * Math.PI * Math.pow(domeRadius.toDouble(), 2.0)
                    extraTasks.add(
                        ExtraTaskResult(
                            title = "طلاء وزخرفة القبة المركزية للمسجد (مساحة: ${String.format(Locale.ENGLISH, "%.1f", domeArea)} م²)",
                            unit = "متر مربع",
                            unitPrice = 1200.0,
                            quantity = domeArea,
                            total = domeArea * 1200.0
                        )
                    )
                    explanationLines.add("• إضافة أعمال طلاء وزخرفة القبة المركزية بمساحة ${String.format(Locale.ENGLISH, "%.1f", domeArea)} م² (قطر $domeRadius م).")
                }
                if (calligraphyLength > 0) {
                    extraTasks.add(
                        ExtraTaskResult(
                            title = "رسم وزخرفة خط عربي يدوي ونقوش إسلامية جدارية",
                            unit = "متر طولي",
                            unitPrice = 1500.0,
                            quantity = calligraphyLength.toDouble(),
                            total = calligraphyLength.toDouble() * 1500.0
                        )
                    )
                    explanationLines.add("• إدراج أعمال كتابة الخط العربي الجداري بطول $calligraphyLength م.")
                }
            }
            "آخر" -> {
                val customDifficulty = params["custom_difficulty"] ?: 1.0f
                val customWaste = params["custom_waste_ratio"] ?: 10f
                
                difficultyMultiplier = customDifficulty.toDouble()
                if (customDifficulty != 1.0f) {
                    explanationLines.add("• تم تطبيق معامل الصعوبة المخصص اليدوي: ×$customDifficulty.")
                }
            }
        }

        return AdaptedCalculationResult(
            difficultyMultiplier = difficultyMultiplier,
            materialMultiplier = materialMultiplier,
            extraTasks = extraTasks,
            explanationLines = explanationLines
        )
    }
}
