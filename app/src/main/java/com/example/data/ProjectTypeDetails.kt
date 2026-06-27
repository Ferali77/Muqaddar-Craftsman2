package com.example.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.Color

data class ProjectTypeSpec(
    val typeNameAr: String,
    val typeNameFr: String,
    val icon: ImageVector,
    val pastelBgColor: Color,
    val iconColor: Color,
    val description: String,
    val characteristics: List<String>,
    val specificParameters: List<SpecificParamSpec>,
    val adaptedCalculationRules: String,
    val baseDifficultyMultiplier: Double = 1.0,
    val defaultMaterialCostFactor: Double = 1.0
)

data class SpecificParamSpec(
    val key: String,
    val labelAr: String,
    val labelFr: String,
    val type: ParamType, // TOGGLE, SLIDER, NUMBER
    val defaultValue: Float,
    val minVal: Float = 0f,
    val maxVal: Float = 100f,
    val valueSuffixAr: String = ""
)

enum class ParamType {
    TOGGLE,
    SLIDER,
    NUMBER
}

object ProjectTypeDetails {
    val specs = listOf(
        ProjectTypeSpec(
            typeNameAr = "شقة",
            typeNameFr = "Appartement",
            icon = Icons.Default.Home,
            pastelBgColor = Color(0xFFE0F2FE), // Blue pastel
            iconColor = Color(0xFF0369A1),
            description = "مساحة سكنية داخلية تتطلب دقة عالية في الفينيسيون والمعالجة الطينية المتعددة الطبقات.",
            characteristics = listOf(
                "ارتفاع أسقف معياري (2.7م - 3.0م)",
                "الحاجة لحماية الحواف والزوايا بشكل مكثف",
                "وجود غرف متعددة وممرات ريفية ضيقة"
            ),
            specificParameters = listOf(
                SpecificParamSpec("has_elevator", "توفر مصعد لنقل المواد والأدوات", "Présence d'ascenseur", ParamType.TOGGLE, 1.0f),
                SpecificParamSpec("finish_level", "مستوى الفينيسيون المطلوبة (1:عادي, 2:ممتاز, 3:لوكس)", "Niveau de finition", ParamType.SLIDER, 2.0f, 1.0f, 3.0f, "")
            ),
            adaptedCalculationRules = "قواعد حساب الشقق ترتكز على مساحات الجدران والأسقف مع احتساب هدر منخفض للمواد (8-10%)، مع خصم دقيق لمساحة النوافذ والأبواب. في حال عدم توفر مصعد وفي طابق مرتفع، يتم تطبيق زيادة بسيطة في تكلفة النقل والمناولة."
        ),
        ProjectTypeSpec(
            typeNameAr = "منزل",
            typeNameFr = "Maison",
            icon = Icons.Default.HomeWork,
            pastelBgColor = Color(0xFFFEE2E2), // Red pastel
            iconColor = Color(0xFFB91C1C),
            description = "منشأة سكنية مستقلة تشمل أعمالاً داخلية وخارجية (واجهات وأسوار)، ومساحات تخزين إضافية.",
            characteristics = listOf(
                "طابق واحد أو طابقين مع درج داخلي",
                "مساحات جدران خارجية معرضة للعوامل الطبيعية",
                "أسقف خرسانية أو قرميدية تتطلب عزل وحماية"
            ),
            specificParameters = listOf(
                SpecificParamSpec("floor_count", "عدد الطوابق الإجمالي", "Nombre d'étages", ParamType.NUMBER, 1.0f, 1.0f, 5.0f, "طوابق"),
                SpecificParamSpec("has_facade", "إدراج طلاء الواجهة الخارجية", "Peinture de façade incluse", ParamType.TOGGLE, 0.0f)
            ),
            adaptedCalculationRules = "يتم حساب أسطح الواجهات الخارجية بشكل منفصل مع معامل صعوبة إضافي (+15%) للعمل في الارتفاعات واستخدام السقالات، مع زيادة نسبة هدر الدهانات الخارجية بنسبة 15% بسبب الرياح وامتصاص الجدران."
        ),
        ProjectTypeSpec(
            typeNameAr = "فيلا",
            typeNameFr = "Villa",
            icon = Icons.Default.HomeWork,
            pastelBgColor = Color(0xFFFAF5FF), // Purple pastel
            iconColor = Color(0xFF7E22CE),
            description = "مسكن راقٍ يتميز بمساحات شاسعة، أسقف مرتفعة، وتفاصيل جبسية وديكورية معقدة للغاية.",
            characteristics = listOf(
                "ارتفاع أسقف كبير (أكثر من 3.2م)",
                "مطالب فنية عالية (دهانات ديكورية، ورق حائط، جبس بورد معقد)",
                "فناء خارجي، أسوار ضخمة، وملاحق متعددة"
            ),
            specificParameters = listOf(
                SpecificParamSpec("gypsum_complexity", "تعقيد الجبس بورد (1:بسيط، 2:متوسط، 3:فاخر جداً)", "Complexité du faux-plafond", ParamType.SLIDER, 2.0f, 1.0f, 3.0f, ""),
                SpecificParamSpec("has_decorative_paint", "استخدام دهانات ديكورية خاصة (خيال، سابلي...)", "Peintures décoratives spéciales", ParamType.TOGGLE, 1.0f)
            ),
            adaptedCalculationRules = "يطبق معامل صعوبة أساسي (1.20x - 1.35x) على أسعار مصنعية الجدران والأسقف نظراً للدقة المطلوبة والارتفاعات. يتم حساب ديكورات الجبس بورد بشكل مستقل لكل متر طولي مع إضافة تكلفة المواد الفاخرة.",
            baseDifficultyMultiplier = 1.2,
            defaultMaterialCostFactor = 1.25
        ),
        ProjectTypeSpec(
            typeNameAr = "محل",
            typeNameFr = "Local commercial",
            icon = Icons.Default.Storefront,
            pastelBgColor = Color(0xFFFEF3C7), // Amber pastel
            iconColor = Color(0xFFD97706),
            description = "مساحة تجارية عامة تتطلب تسليماً سريعاً، ومواد دهان شديدة المقاومة للاحتكاك وسهلة الغسيل.",
            characteristics = listOf(
                "أبواب زجاجية وواجهات عرض واسعة",
                "أرضيات صلبة ذات حركة مرورية عالية جداً",
                "إضاءة مركزية مدمجة تتطلب معالجة أسطح ممتاز"
            ),
            specificParameters = listOf(
                SpecificParamSpec("commercial_epoxy", "الحاجة لطلاء أرضيات إيبوكسي صناعي", "Sol en résine Epoxy", ParamType.TOGGLE, 0.0f),
                SpecificParamSpec("has_showcase", "احتساب مساحة واجهة العرض الزجاجية لخصمها", "Façade vitrée (à déduire)", ParamType.TOGGLE, 1.0f)
            ),
            adaptedCalculationRules = "تتميز المحلات بقواعد خصم واسعة لواجهات العرض الزجاجية. يتم احتساب دهانات قابلة للغسل ومقاومة للاحتكاك الشديد مع معامل هدر للمواد بنسبة 12% لتغطية زوايا الديكورات وعلب العرض."
        ),
        ProjectTypeSpec(
            typeNameAr = "مكتب",
            typeNameFr = "Bureau",
            icon = Icons.Default.Business,
            pastelBgColor = Color(0xFFECFDF5), // Emerald pastel
            iconColor = Color(0xFF047857),
            description = "بيئة عمل احترافية هادئة تستهدف تقسيماً وظيفياً ممتازاً ودهانات مريحة للعين ومقاومة لبهوت الألوان.",
            characteristics = listOf(
                "تقسيمات جدران جافة (بلاكو بلاتر / جبس بورد)",
                "أسقف مستعارة عازلة للصوت والحرارة",
                "ممرات وحجرات إدارية متعددة"
            ),
            specificParameters = listOf(
                SpecificParamSpec("drywall_partitions", "حجم فواصل الجبس بورد المطلوبة (م²)", "Cloisons amovibles en plaques de plâtre (m²)", ParamType.NUMBER, 0.0f, 0.0f, 1000.0f, "م²"),
                SpecificParamSpec("eco_paint", "استخدام دهانات صديقة للبيئة وعديمة الرائحة", "Peinture écologique sans odeur", ParamType.TOGGLE, 1.0f)
            ),
            adaptedCalculationRules = "يتم التركيز على حساب فواصل البلاكو بلاتر ذات الوجهين مع حساب معالجة الفواصل بـ (الشاش والمعجون الدقيق). يطبق خصم خاص للمساحات المكتبية المفتوحة الكبيرة (Open Space)."
        ),
        ProjectTypeSpec(
            typeNameAr = "عيادة",
            typeNameFr = "Cabinet médical",
            icon = Icons.Default.Business,
            pastelBgColor = Color(0xFFF0FDF4), // Mint green
            iconColor = Color(0xFF16A34A),
            description = "منشأة صحية تخضع لمعايير نظافة صارمة وشروط تعقيم قصوى تتطلب مواد خاصة مضادة للبكتيريا.",
            characteristics = listOf(
                "دهانات حوائط مضادة للبكتيريا والفطريات وقابلة للغسيل والتعقيم الكيميائي",
                "أرضيات إيبوكسي أو فينيل مستمرة وخالية من الفواصل لمنع تراكم الجراثيم",
                "زوايا دائرية مسهلة لعمليات التنظيف والمسح اليومي"
            ),
            specificParameters = listOf(
                SpecificParamSpec("antibacterial_paint", "تفعيل مواصفة الدهان الطبي المضاد للبكتيريا", "Peinture antibactérienne", ParamType.TOGGLE, 1.0f),
                SpecificParamSpec("seamless_epoxy_floor", "تجهيز أرضية إيبوكسي طبية مستمرة", "Sol résine époxy hygiénique", ParamType.TOGGLE, 1.0f)
            ),
            adaptedCalculationRules = "يتم ضرب تكلفة مواد طلاء الجدران والأسقف بمعامل (1.30x) نظراً لارتفاع سعر الدهان المضاد للبكتيريا المعتمد طبياً. ترتفع تكلفة مصنعية ومواد الأرضية لتشمل تسوية وضبط الإيبوكسي الذاتي الاستواء.",
            baseDifficultyMultiplier = 1.15,
            defaultMaterialCostFactor = 1.3
        ),
        ProjectTypeSpec(
            typeNameAr = "مدرسة",
            typeNameFr = "École",
            icon = Icons.Default.School,
            pastelBgColor = Color(0xFFEFF6FF), // Blueish pastel
            iconColor = Color(0xFF1D4ED8),
            description = "مرفق تعليمي ذو كثافة طلابية عالية يتطلب أقصى درجات التحمل ضد الخدوش والمسح المستمر.",
            characteristics = listOf(
                "ممرات شاسعة وقاعات دراسية ذات جدران طويلة",
                "جدران سفلية مطلية بدهان زيتي أو إيبوكسي مقاوم للخدش والصدمات",
                "مساحات مخصصة للسبورات التوضيحية"
            ),
            specificParameters = listOf(
                SpecificParamSpec("wall_guard_height", "ارتفاع طلاء الزيت المقاوم للصدمات بالجدار (متر)", "Hauteur de protection murale (m)", ParamType.SLIDER, 1.2f, 0.5f, 2.0f, "متر"),
                SpecificParamSpec("large_volume_discount", "تطبيق خصم المساحات الكبيرة للمشروعات الحكومية/العامة", "Remise grand volume", ParamType.TOGGLE, 1.0f)
            ),
            adaptedCalculationRules = "يتم تقسيم الجدران أفقياً: الجزء السفلي (حتى 1.2م أو 1.5م) يحسب بدهانات زيتية أو إيبوكسي صلبة، والجزء العلوي بدهانات مائية. يطبق خصم كميات يتراوح بين (5% إلى 12%) في حال تجاوز المساحة الإجمالية 800 متر مربع."
        ),
        ProjectTypeSpec(
            typeNameAr = "مطعم",
            typeNameFr = "Restaurant",
            icon = Icons.Default.Restaurant,
            pastelBgColor = Color(0xFFFFF7ED), // Orange pastel
            iconColor = Color(0xFFEA580C),
            description = "مكان لتقديم الأغذية يجمع بين روعة التصميم في الصالة والصرامة الفنية ومقاومة الدهون والحرائق في المطبخ.",
            characteristics = listOf(
                "مطبخ ذو رطوبة وحرارة عالية وتراكم دهون كثيف",
                "صالة استقبال ذات لمسات معمارية وديكورية عصرية لجذب الزبائن",
                "أرضيات مطابخ مانعة للانزلاق وسهلة التنظيف بالماء الساخن"
            ),
            specificParameters = listOf(
                SpecificParamSpec("kitchen_fire_retardant", "دهان مقاوم للحريق والرطوبة في منطقة المطبخ", "Peinture coupe-feu cuisine", ParamType.TOGGLE, 1.0f),
                SpecificParamSpec("lounge_decorative_walls", "مساحة الحوائط الديكورية الفاخرة في صالة الطعام (م²)", "Murs décoratifs salle (m²)", ParamType.NUMBER, 20f, 0f, 500f, "م²")
            ),
            adaptedCalculationRules = "يتم عزل مساحة المطبخ وحسابها بشكل منفصل بقوانين مواد مضادة للرطوبة ومقاومة لالتصاق الدهون وحرائق الغاز. صالة الاستقبال تحسب بأسعار الديكورات الفاخرة مع زيادة هدر الدهان بنسبة 10% للألوان الغامقة والحديثة.",
            baseDifficultyMultiplier = 1.1,
            defaultMaterialCostFactor = 1.15
        ),
        ProjectTypeSpec(
            typeNameAr = "مقهى",
            typeNameFr = "Café",
            icon = Icons.Default.Storefront,
            pastelBgColor = Color(0xFFFDF8E2), // Warm cream
            iconColor = Color(0xFF854D0E),
            description = "فضاء ترفيهي واجتماعي يرتكز بشكل جوهري على الهوية البصرية الفريدة وتناسق الألوان الجريئة والمريحة.",
            characteristics = listOf(
                "جدران ذات طابع فني (سبورة طباشيرية، حوائط خرسانية مكشوفة)",
                "ديكورات أسقف مفتوحة تظهر الأنابيب والقنوات مطلية باللون الأسود",
                "إضاءة دافئة تكشف عيوب المعجون وتتطلب ضبطاً ممتازاً"
            ),
            specificParameters = listOf(
                SpecificParamSpec("exposed_ceiling_paint", "طلاء الأسقف المفتوحة المكشوفة (أنابيب وأسلاك)", "Plafond industriel ouvert", ParamType.TOGGLE, 0.0f),
                SpecificParamSpec("chalkboard_wall_area", "مساحة جدار السبورة الطباشيرية الخاص (م²)", "Peinture ardoise tableau (m²)", ParamType.NUMBER, 5f, 0f, 50f, "م²")
            ),
            adaptedCalculationRules = "يتم حساب الأسقف المفتوحة والمكشوفة (Industrial Theme) مع زيادة بنسبة 25% في استهلاك الدهان بالرش نظراً لتعرجات الأنابيب والأسلاك الكهربائية، مع معامل صعوبة إضافي نظراً للدقة المطلوبة."
        ),
        ProjectTypeSpec(
            typeNameAr = "مستودع",
            typeNameFr = "Entrepôt",
            icon = Icons.Default.Business,
            pastelBgColor = Color(0xFFF1F5F9), // Slate pastel
            iconColor = Color(0xFF475569),
            description = "مساحة تخزين عملاقة تتميز بارتفاعات أسقف شاهقة وجدران صلبة وأرضيات خرسانية معالجة مخصصة للأوزان الثقيلة.",
            characteristics = listOf(
                "ارتفاعات أسقف فائقة تتراوح بين 5 إلى 12 متر",
                "استخدام كثيف للسقالات المتحركة والرافعات الهيدروليكية",
                "تجهيز وصقل الأرضيات بالإيبوكسي المقاوم للضغط والزيوت"
            ),
            specificParameters = listOf(
                SpecificParamSpec("scaffolding_height", "أقصى ارتفاع للعمل (أمتار) - يتطلب سقالات خاصة", "Hauteur maximale de travail (m)", ParamType.SLIDER, 6.0f, 3.0f, 15.0f, "متر"),
                SpecificParamSpec("industrial_floor_coating", "طلاء أرضيات إيبوكسي فائق التحمل (سمك 2-3 ملم)", "Résine époxy auto-lissante industrielle", ParamType.TOGGLE, 1.0f)
            ),
            adaptedCalculationRules = "تتميز المستودعات بخصم خاص (-15%) على مصنعية الجدران العادية لسهولة دهانها بالرش، لكن تضاف رسوم إيجار ونصب السقالات إذا تجاوز الارتفاع 4 أمتار. يتم احتساب مواد طلاء الأرضيات الإيبوكسي بدقة بالغة بالاعتماد على الوزن الإجمالي وسماكة الطلاء.",
            baseDifficultyMultiplier = 0.9,
            defaultMaterialCostFactor = 1.1
        ),
        ProjectTypeSpec(
            typeNameAr = "ورشة",
            typeNameFr = "Atelier",
            icon = Icons.Default.Build,
            pastelBgColor = Color(0xFFFFF1F2), // Rose pastel
            iconColor = Color(0xFFE11D48),
            description = "موقع إنتاج وصيانة يعاني من الغبار والزيوت وتناثر الشظايا، يتطلب طلاءات حماية متخصصة.",
            characteristics = listOf(
                "جدران معرضة للأوساخ والزيوت تتطلب حماية إيبوكسية جزئية",
                "أرضيات مانعة للانزلاق ومقاومة لانسكاب المواد الكيميائية والزيوت",
                "ضرورة طلاء خطوط الأمان والممرات التحذيرية باللونين الأصفر والأسود"
            ),
            specificParameters = listOf(
                SpecificParamSpec("safety_lines_length", "أطوال خطوط الأمان والتحذير الأرضية (متر طولي)", "Lignes de sécurité au sol (ml)", ParamType.NUMBER, 15f, 0f, 200f, "متر طولي"),
                SpecificParamSpec("oil_resistant_sealer", "معالجة الجدران بدهان أساس عازل ومقاوم للرطوبة والزيوت", "Primaire d'accrochage spécial", ParamType.TOGGLE, 1.0f)
            ),
            adaptedCalculationRules = "يتم احتساب خطوط الأمان الأرضية بسعر مستقل للمتر الطولي. يوصى بزيادة طبقات معجون التأسيس واستخدام دهان أساس (سيلر) عازل كلياً لمنع تسرب الشحوم والزيوت عبر الجدران والمسام الخرسانية."
        ),
        ProjectTypeSpec(
            typeNameAr = "مصنع",
            typeNameFr = "Usine",
            icon = Icons.Default.Factory,
            pastelBgColor = Color(0xFFFFF7ED), // Orange pastel
            iconColor = Color(0xFFC2410C),
            description = "منشأة صناعية كبرى تخضع لقوانين أمان وسلامة صارمة ومقاومة للحرارة والكيماويات القوية.",
            characteristics = listOf(
                "مساحات شاسعة وبيئات عمل قاسية ومختلفة درجات الحرارة",
                "خطوط إنتاج تتطلب طلاءات جدران عاكسة ومسهلة للرؤية ومقاومة للغبار",
                "ممرات طوارئ ورموز سلامة مرسومة بدقة على الجدران والأرضيات"
            ),
            specificParameters = listOf(
                SpecificParamSpec("chemical_resistance_level", "مستوى المقاومة الكيميائية المطلوب (1 إلى 3)", "Niveau de résistance chimique", ParamType.SLIDER, 2f, 1f, 3f, ""),
                SpecificParamSpec("has_heat_paint", "استخدام دهانات مقاومة لدرجات الحرارة العالية", "Peintures résistantes à la chaleur", ParamType.TOGGLE, 0.0f)
            ),
            adaptedCalculationRules = "يطبق معامل صعوبة وأمان في التسعير (+20%) بسبب الالتزام ببروتوكولات السلامة والعمل حول الآلات النشطة. يتم احتساب الدهانات الحرارية والكيماوية المتخصصة بأسعارها الحقيقية المرتفعة مع معامل هدر 15%."
        ),
        ProjectTypeSpec(
            typeNameAr = "فندق",
            typeNameFr = "Hôtel",
            icon = Icons.Default.Hotel,
            pastelBgColor = Color(0xFFEEF2FF), // Indigo pastel
            iconColor = Color(0xFF4338CA),
            description = "مرفق ضيافة فاخر يتميز بتكرار الغرف المتطابقة (الأجنحة) والحاجة لتشطيبات غاية في الفخامة والجمال.",
            characteristics = listOf(
                "أجنحة وغرف نوم متكررة تسهل على الحرفي سرعة التنفيذ",
                "لوبي وممرات رئيسية ضخمة تتطلب أسقف مستعارة معقدة وتأثيرات بصرية راقية",
                "مواد عزل صوت مدمجة مع جدران الجبس بورد"
            ),
            specificParameters = listOf(
                SpecificParamSpec("room_replication_count", "عدد الغرف المتطابقة لخصم التكرار", "Nombre de chambres identiques", ParamType.NUMBER, 10f, 1f, 200f, "غرفة"),
                SpecificParamSpec("has_wallpaper", "تطبيق تركيب ورق حائط فاخر", "Pose de papier peint", ParamType.TOGGLE, 0.0f)
            ),
            adaptedCalculationRules = "يتم تقديم خصم تكرار كبير (-15% إلى -20%) على مصنعيات الغرف المتطابقة نظراً لسهولة تنقل الحرفي واعتياده على التصميم، بينما تسعر حوائط اللوبي والواجهات بأسعار مستقلة ومرتفعة تعكس صعوبتها."
        ),
        ProjectTypeSpec(
            typeNameAr = "قاعة حفلات",
            typeNameFr = "Salle des fêtes",
            icon = Icons.Default.Celebration,
            pastelBgColor = Color(0xFFFDF2F8), // Pink pastel
            iconColor = Color(0xFFDB2777),
            description = "فضاء شاسع ومفتوح مخصص للاحتفالات يتطلب ديكورات جبسية فاخرة وأنظمة إضاءة مخفية بالكامل.",
            characteristics = listOf(
                "أسقف شاسعة خالية من الأعمدة بارتفاعات تزيد عن 4.5 متر",
                "أعمال ديكورية زخرفية بالغة التعقيد وحجرات خلفية متعددة",
                "جدران مغلفة بألياف عازلة للصوت والصدى ومكسوة بدهانات برّاقة"
            ),
            specificParameters = listOf(
                SpecificParamSpec("ceiling_height", "ارتفاع السقف الفعلي للقاعة (متر)", "Hauteur de plafond de la salle (m)", ParamType.SLIDER, 5.0f, 3.0f, 10.0f, "متر"),
                SpecificParamSpec("luxury_decor_factor", "مستوى الفخامة والزخرفة (1:متوسط، 2:فاخر، 3:ملكي)", "Facteur de luxe et d'ornementation", ParamType.SLIDER, 2f, 1f, 3f, "")
            ),
            adaptedCalculationRules = "يتم احتساب الأسقف الشاسعة بمعامل صعوبة إضافي للارتفاع واستخدام رافعات وسقالات. تضاعف تكلفة دهانات الفينيسيون بسبب الإضاءات الجانبية القوية والموجهة والتي تكشف أدق عيوب المعجون والأسطح."
        ),
        ProjectTypeSpec(
            typeNameAr = "مسجد",
            typeNameFr = "Mosque",
            icon = Icons.Default.Mosque,
            pastelBgColor = Color(0xFFF0FDF4), // Emerald green light pastel
            iconColor = Color(0xFF15803D),
            description = "مكان عبادة مقدس يتميز بالمحراب، القبة، والمنبر، ويتطلب دقة زخرفية فائقة، خطوطاً عربية أصيلة، وسجاداً عالي الجودة.",
            characteristics = listOf(
                "جدران مرتفعة وأسقف شاهقة مع قبة مركزية ضخمة",
                "خطوط وزخارف إسلامية ونقوش خط عربي حول القباب والمحراب",
                "أرضية سجاد مبطن عازل للصوت والحرارة ومريح للمصلين"
            ),
            specificParameters = listOf(
                SpecificParamSpec("dome_radius", "قطر القبة المركزية بالأمتار (إذا وجدت)", "Diamètre du dôme central (m)", ParamType.SLIDER, 0f, 0f, 20f, "متر"),
                SpecificParamSpec("calligraphy_length", "أطوال كتابة الخط العربي والزخارف الإسلامية (متر طولي)", "Longueur de calligraphie islamique (ml)", ParamType.NUMBER, 10f, 0f, 200f, "متر طولي")
            ),
            adaptedCalculationRules = "القباب والمحراب والمنبر يتم تقدير أعمال دهانها ومعالجتها الجبسية كأسعار استثنائية مقطوعة أو بمعامل صعوبة مرتفع (1.50x). يتم احتساب كتابة الخط العربي والزخارف الإسلامية بسعر خاص لكل متر طولي أو حسب صعوبة التصميم المطلوب.",
            baseDifficultyMultiplier = 1.3,
            defaultMaterialCostFactor = 1.2
        ),
        ProjectTypeSpec(
            typeNameAr = "آخر",
            typeNameFr = "Autre type",
            icon = Icons.Default.Construction,
            pastelBgColor = Color(0xFFF8FAFC), // Slate-gray light
            iconColor = Color(0xFF64748B),
            description = "مشروع مخصص كلياً غير مدرج في التصنيفات الأساسية، مما يمنح مرونة تامة لتعيين الشروط والأسعار المخصصة.",
            characteristics = listOf(
                "خصائص معمارية مرنة يحددها المستخدم يدوياً",
                "إمكانية إدخال نسب استهلاك ومقادير هدر متغيرة",
                "ملائم للمباني السكنية المدمجة أو المواقع الهجينة"
            ),
            specificParameters = listOf(
                SpecificParamSpec("custom_difficulty", "معامل صعوبة إجمالي مخصص للمشروع", "Facteur de difficulté personnalisé", ParamType.SLIDER, 1.0f, 0.5f, 2.5f, "ضعف"),
                SpecificParamSpec("custom_waste_ratio", "نسبة الهدر الاحتياطية المطلوبة", "Marge de perte personnalisée (%)", ParamType.SLIDER, 10f, 0f, 50f, "%")
            ),
            adaptedCalculationRules = "تخضع جميع الحسابات لخيارات المستخدم المباشرة ومضاعف الصعوبة المخصص الذي يقوم بإدخاله يدوياً ليضرب في كافة أسعار الجدران والأسقف والأرضيات."
        )
    )

    fun getSpecForType(type: String): ProjectTypeSpec {
        return specs.find { it.typeNameAr == type || it.typeNameFr.equals(type, ignoreCase = true) }
            ?: specs.last() // last is "آخر"
    }
}
