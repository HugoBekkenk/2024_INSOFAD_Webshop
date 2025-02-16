package com.example.gamewebshop.utils;
import com.example.gamewebshop.dao.*;
import com.example.gamewebshop.models.*;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Component
public class Seeder {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final VariantRepository variantRepository;
    private final OptionsRepository optionsRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final PromoCodeRepository promoCodeRepository;
    private final GiftCardDAO giftCardDAO;
    public Seeder(UserRepository userRepository, ProductRepository productRepository, VariantRepository variantRepository, OptionsRepository optionsRepository, CategoryRepository categoryRepository, PasswordEncoder passwordEncoder, PromoCodeRepository promoCodeRepository, GiftCardDAO giftCardDAO) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.variantRepository = variantRepository;
        this.optionsRepository = optionsRepository;
        this.categoryRepository = categoryRepository;
        this.passwordEncoder = passwordEncoder;
        this.promoCodeRepository = promoCodeRepository;
        this.giftCardDAO = giftCardDAO;
    }
    @EventListener
    public void seed(ContextRefreshedEvent event) {
        List<CustomUser> users = this.userRepository.findAll();
        if (!users.isEmpty()) {
            return;
        }
        this.seedUsers();
        this.seedProducts();
        this.seedGiftCards();
        savePromoCodes();
    }
    private void seedUsers(){
        String encodedPassword = passwordEncoder.encode("Test123!");
        CustomUser user = new CustomUser("testName", "testInfix", "testLastName", "test@mail.com", encodedPassword, "user");
        this.userRepository.save(user);
        String encodedAdminPassword = passwordEncoder.encode("IJS+N3u#(Z0Ds]3~#qZw");
        CustomUser admin = new CustomUser("AdminName", "AdminInfix", "AdminLastName", "admin@mail.com", encodedAdminPassword, "admin");
        this.userRepository.save(admin);
    }
    private void seedProducts(){
        ArrayList<Category> categories = new ArrayList<>();
        Category FPS = new Category("FPS");
        Category fighter = new Category("Fighter");
        Category soulsLike = new Category("Souls-like");
        Category action = new Category("Action");
        Category creative = new Category("Creative");
        Category openWorld = new Category("Open world");
        Category racing = new Category("Racing");
        categories.add(FPS);
        categories.add(fighter);
        categories.add(soulsLike);
        categories.add(action);
        categories.add(creative);
        categories.add(openWorld);
        categories.add(racing);
        this.categoryRepository.saveAll(categories);
        ArrayList<Product> products = new ArrayList<>();
        Product rainbowSixSiege = new Product(
                "Tom Clancy's Rainbow Six Siege",
                "Tom Clancy's Rainbow Six® Siege is an elite, tactical team-based shooter where superior planning and execution triumph.",
                15.99,
                "https://store.ubisoft.com/on/demandware.static/-/Sites-masterCatalog/default/dw63e24d90/images/large/56c494ad88a7e300458b4d5a.jpg",
                FPS,
                "OS *: Originally released for Windows 7, the game can be played on Windows 10 and Windows 11 OS" +
                        "Processor: Intel Core i5-2500K @ 3.3 GHz or better or AMD FX-8120 @ 3.1 Ghz or better\n" +
                        "Memory: 8 GB RAM\n" +
                        "Graphics: NVIDIA GeForce GTX 670 or AMD Radeon HD 7970",
                "1-Dec-2015",
                "Ubisoft",
                "PRODUCT"
        );
        Product mortalKombar1 = new Product(
                "Mortal Kombat 1",
                "Discover a reborn Mortal Kombat™ Universe created by the Fire God Liu Kang. Mortal Kombat™ 1 ushers in a new era of the iconic franchise with a new fighting system, game modes, and fatalities!",
                34.99,
                "https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcSAMOK3cjH0oK89AraaQ_DJQ3EyioLRJxCnq0X2lLPXDisjyn1XOuwGkRLht5b1c7d7G6Uk2w",
                fighter,
                "Requires a 64-bit processor and operating system\n" +
                        "OS: Windows 10/11 64-bit\n" +
                        "Processor: Intel® Core™ i5-8400 | AMD Ryzen™ 5 3600X\n" +
                        "Memory: 8 GB RAM\n" +
                        "Graphics: Nvidia GeForce® GTX 1080 Ti or AMD Radeon™ RX 5700 XT or Intel® Arc™ A770\n" +
                        "DirectX: Version 12\n" +
                        "Storage: 140 GB available space\n" +
                        "Additional Notes: SSD",
                "19 Sep, 2023",
                "Warner Bros. Games", "PRODUCT");
        Product eldenRing = new Product(
                "Elden Ring",
                "THE NEW FANTASY ACTION RPG. Rise, Tarnished, and be guided by grace to brandish the power of the Elden Ring and become an Elden Lord in the Lands Between.",
                47.99,
                "https://upload.wikimedia.org/wikipedia/en/b/b9/Elden_Ring_Box_art.jpg",
                soulsLike,
                "Requires a 64-bit processor and operating system\n" +
                        "OS: Windows 10/11\n" +
                        "Processor: INTEL CORE I7-8700K or AMD RYZEN 5 3600X\n" +
                        "Memory: 16 GB RAM\n" +
                        "Graphics: NVIDIA GEFORCE GTX 1070 8 GB or AMD RADEON RX VEGA 56 8 GB",
                "25-Feb-2022",
                "FromSoftware Inc., Bandai Namco Entertainment", "PRODUCT");
        Product helldivers2 = new Product(
                "Helldivers 2",
                "The Galaxy’s Last Line of Offence. Enlist in the Helldivers and join the fight for freedom across a hostile galaxy in a fast, frantic, and ferocious third-person shooter.",
                39.99,
                "https://image.api.playstation.com/vulcan/ap/rnd/202309/0718/60ebb0f1f65149baa3c3ea07b08f8595c17e7759fea79e1c.jpg",
                action,
                "Requires a 64-bit processor and operating system\n" +
                        "OS: Windows 10\n" +
                        "Processor: Intel Core i7-9700K or AMD Ryzen 7 3700X\n" +
                        "Memory: 16 GB RAM\n" +
                        "Graphics: NVIDIA GeForce RTX 2060 or AMD Radeon RX 6600XT",
                "8-Feb-2024",
                "PlayStation PC LLC", "PRODUCT");
        Product codmw3 = new Product(
                "Call of Duty®: Modern Warfare® III",
                "In the direct sequel to the record-breaking Call of Duty®: Modern Warfare® II, Captain Price and Task Force 141 face off against the ultimate threat.",
                69.99,
                "https://m.media-amazon.com/images/M/MV5BYmNjZDM0M2EtZTFiYy00YjJlLWIzNDUtNjhhMjk2ODNkYzFlXkEyXkFqcGdeQXVyMTcyNjA2NzMx._V1_FMjpg_UX1000_.jpg",
                FPS,
                "Requires a 64-bit processor and operating system\n" +
                        "OS: Windows® 10 64 Bit (latest update) or Windows® 11 64 Bit (latest update)\n" +
                        "Processor: Intel® Core™ i7-6700K or AMD Ryzen™ 5 1600X\n" +
                        "Memory: 16 GB RAM\n" +
                        "Graphics: NVIDIA® GeForce® GTX 1080Ti / RTX 3060 or AMD Radeon™ RX 6600XT or Intel® Arc™ A770",
                "10-Nov-2023",
                "Activision", "PRODUCT");
        Product battlefield = new Product(
                "Battlefield 3",
                "Enjoy total freedom to fight the way you want. Explore 29 massive multiplayer maps and use loads of vehicles, weapons, and gadgets to help you turn up the heat.",
                39.99,
                "https://upload.wikimedia.org/wikipedia/en/6/69/Battlefield_3_Game_Cover.jpg",
                FPS,
                "OS *: Windows 7 64-bit\n" +
                        "Processor: Quad-core CPU\n" +
                        "Memory: 4 GB RAM\n" +
                        "Graphics: Graphics Card: DirectX 11 compatible with 1024 MB RAM (NVIDIA GeForce GTX 560 or ATI Radeon 6950)",
                "28-Oct-2011",
                "Electronic Arts", "PRODUCT");
        Product codBlackOps1 = new Product(
                "Call of Duty®: Black Ops",
                "The biggest first-person action series of all time and the follow-up to critically acclaimed Call of Duty®: Modern Warfare 2 returns with Call of Duty®: Black Ops.",
                39.99,
                "https://upload.wikimedia.org/wikipedia/en/0/02/CoD_Black_Ops_cover.png",
                FPS,
                "OS *: Windows® Vista / XP / 7\n" +
                        "Processor: Intel® Core™2 Duo E6600 or AMD Phenom™ X3 8750 or better\n" +
                        "Memory: 2GB\n" +
                        "Graphics: Shader 3.0 or better 256MB NVIDIA® GeForce® 8600GT / ATI Radeon® X1950Pro or better\n" +
                        "DirectX®: DirectX® 9.0c\n" +
                        "Hard Drive: 12GB*\n" +
                        "Sound: DirectX® 9.0c-compatible",
                "9-Nov-2010",
                "Activision", "PRODUCT");
        Product minecraft = new Product(
                "Minecraft",
                "Minecraft is a 2011 sandbox game developed by Mojang Studios and originally released in 2009. The game was created by Markus \"Notch\" Persson in the Java programming language.",
                29.99,
                "https://upload.wikimedia.org/wikipedia/en/5/51/Minecraft_cover.png",
                creative,
                "CPU: Intel Core i5-4690 3.5GHz / AMD A10-7800 APU 3.5 GHz or equivalent\n" +
                        "CPU SPEED: Info\n" +
                        "RAM: 4 GB\n" +
                        "VIDEO CARD: GeForce 700 Series or AMD Radeon Rx 200 Series (excluding integrated chipsets) with OpenGL 4.5\n" +
                        "DEDICATED VIDEO RAM: 256 MB\n" +
                        "PIXEL SHADER: 5.0\n" +
                        "VERTEX SHADER: 5.0\n" +
                        "OS: Windows 10\n" +
                        "FREE DISK SPACE: 4 GB",
                "18-Nov-2011",
                "Mojang Studios", "PRODUCT");
        Product fifa24 = new Product(
                "EA SPORTS FC™ 24",
                "EA SPORTS FC™ 24 welcomes you to The World’s Game: the most true-to-football experience ever with HyperMotionV, PlayStyles optimised by Opta, and an enhanced Frostbite™ Engine.",
                69.99,
                "https://store-images.s-microsoft.com/image/apps.62211.14281959400456729.871028c8-df4c-403f-826e-d4deb249dc43.b5be9148-05ab-41a0-af06-5a2e7e03d0f3",
                creative,
                "OS: Windows 10 - 64-Bit\n" +
                        "Processor: Intel Core i7-6700 @ 3.40GHz or AMD Ryzen 7 2700X @ 3.7 GHZ\n" +
                        "Memory: 12 GB RAM\n" +
                        "Graphics: NVIDIA GeForce GTX 1660 or AMD RX 5600 XT\n" +
                        "DirectX: Version 12",
                "29 Sep, 2023",
                "Electronic Arts", "PRODUCT");
        Product borderlands = new Product(
                "Borderlands 3",
                "The original shooter-looter returns, packing bazillions of guns and a mayhem-fueled adventure! Blast through new worlds and enemies as one of four new Vault Hunters.",
                59.99,
                "https://upload.wikimedia.org/wikipedia/en/2/21/Borderlands_3_cover_art.jpg",
                creative,
                "Requires a 64-bit processor and operating system\n" +
                        "OS *: Windows 7/10 (latest service pack)\n" +
                        "Processor: AMD Ryzen™ 5 2600 (Intel i7-4770)\n" +
                        "Memory: 16 GB RAM\n" +
                        "Graphics: AMD Radeon™ RX 590 or NVIDIA GeForce GTX 1060 6GB",
                "13 Mar, 2020",
                "2K", "PRODUCT");
        Product gta6 = new Product(
                "Grand Theft Auto VI",
                "Grand Theft Auto VI is an upcoming action-adventure game in development by Rockstar Games. It is due to be the eighth main Grand Theft Auto game, following Grand Theft Auto V, and the sixteenth entry overall.",
                59.99,
                "https://upload.wikimedia.org/wikipedia/en/4/46/Grand_Theft_Auto_VI.png",
                openWorld,
                "Memory: 16 GB.\n" +
                        "Graphics Card: NVIDIA GeForce RTX 2080 SUPER.\n" +
                        "CPU: Intel Core i7-8700K.\n" +
                        "File Size: 150 GB.\n" +
                        "OS: up to Windows 11.",
                "Comming Soon",
                "Rockstar Games", "PRODUCT");
        Product gtaSA = new Product(
                "Grand Theft Auto: San Andreas",
                "Five years ago Carl Johnson escaped from the pressures of life in Los Santos, San Andreas... a city tearing itself apart with gang trouble, drugs and corruption.",
                59.99,
                "https://upload.wikimedia.org/wikipedia/en/thumb/c/c4/GTASABOX.jpg/220px-GTASABOX.jpg",
                openWorld,
                "Processor: Intel Pentium 4 or AMD Athlon XP Processor\n" +
                        "Memory: 384MB of RAM (the more the better!)\n" +
                        "Graphics: 128MB (or greater) Video Card (Geforce 6 Series Recommended)\n" +
                        "Hard Drive: 4.7GB of free hard disk space (full install)",
                "26-Oct-2004",
                "Rockstar Games", "PRODUCT");
        Product crashTeamRacing = new Product(
                "Crash Team Racing Nitro-Fueled",
                "Crash Team Racing Nitro-Fueled is a 2019 kart racing game developed by Beenox and published by Activision.",
                29.99,
                "https://upload.wikimedia.org/wikipedia/en/3/36/Crash_Team_Racing_Nitro-Fueled_cover_art.jpg",
                racing,
                "None",
                "20-June-2019",
                "Activision", "PRODUCT");
        rainbowSixSiege.setStock(1);
        products.add(rainbowSixSiege);
        products.add(mortalKombar1);
        products.add(eldenRing);
        products.add(helldivers2);
        products.add(codmw3);
        products.add(battlefield);
        products.add(codBlackOps1);
        products.add(minecraft);
        products.add(fifa24);
        products.add(borderlands);
        products.add(gta6);
        products.add(gtaSA);
        products.add(crashTeamRacing);
        for (Product product : products){
            this.productRepository.save(product);
            if (product == rainbowSixSiege) {
                Variant platformVariant = new Variant("Platform", product);
                Variant editionVariant = new Variant("Edition", product);
                this.variantRepository.save(platformVariant);
                this.variantRepository.save(editionVariant);
                Options pcOption = new Options("Pc", 0, platformVariant);
                Options switchOption = new Options("Switch", 3, platformVariant);
                Options xboxOption = new Options("Xbox", 1, platformVariant);
                Options ps4Option = new Options("Ps4", 2, platformVariant);
                Options defaultOption = new Options("Defualt", 0, editionVariant);
                Options limitedOption = new Options("Definitive", 10, editionVariant);
                Options ultimateOption = new Options("Ultimate", 15, editionVariant);
                this.optionsRepository.save(pcOption);
                this.optionsRepository.save(xboxOption);
                this.optionsRepository.save(ps4Option);
                this.optionsRepository.save(switchOption);
                this.optionsRepository.save(defaultOption);
                this.optionsRepository.save(limitedOption);
                this.optionsRepository.save(ultimateOption);
            }
            else {
                Variant platformVariant = new Variant("Platform", product);
                Variant editionVariant = new Variant("Edition", product);
                this.variantRepository.save(platformVariant);
                this.variantRepository.save(editionVariant);
                Options pcOption = new Options("Pc", 0, platformVariant);
                Options xboxOption = new Options("Xbox", 1, platformVariant);
                Options ps4Option = new Options("Ps4", 2, platformVariant);
                Options defaultOption = new Options("Default", 0, editionVariant);
                Options limitedOption = new Options("Limited", 10, editionVariant);
                this.optionsRepository.save(pcOption);
                this.optionsRepository.save(xboxOption);
                this.optionsRepository.save(ps4Option);
                this.optionsRepository.save(defaultOption);
                this.optionsRepository.save(limitedOption);
            }
        }
    }
    private void savePromoCodes() {

        PromoCode promoCode1 = new PromoCode("MAY2024", 15, LocalDateTime.of(2024, 8, 31, 23, 59, 59), 100, PromoCode.PromoCodeType.PERCENTAGE);
        PromoCode promoCode2 = new PromoCode("PROMO1PLUS1", 0, LocalDateTime.of(2024, 12, 31, 23, 59, 59), 50, PromoCode.PromoCodeType.FIXED_AMOUNT); // Updated for 1+1 promotion
        PromoCode promoCode3 = new PromoCode("GIFT2024", 20, LocalDateTime.of(2024, 11, 15, 23, 59, 59), 200, PromoCode.PromoCodeType.PERCENTAGE);
        PromoCode promoCode4 = new PromoCode("20EUROS", 20.0, LocalDateTime.of(2025, 5, 3, 2, 40, 1), 50, PromoCode.PromoCodeType.FIXED_AMOUNT);
        PromoCode promoCode5 = new PromoCode("PROMO1PLUS1", 10.0, LocalDateTime.of(2025, 5, 3, 2, 40, 1), 50, PromoCode.PromoCodeType.FIXED_AMOUNT);
        PromoCode promoCode6 = new PromoCode("PROMOXBOX", 44, LocalDateTime.of(2024, 11, 15, 23, 59, 59), 200, PromoCode.PromoCodeType.PERCENTAGE);
        PromoCode promoCode7 = new PromoCode("PROMOPCDEFAULT", 15, LocalDateTime.of(2024, 8, 31, 23, 59, 59), 100, PromoCode.PromoCodeType.PERCENTAGE);

        promoCodeRepository.save(promoCode1);
        promoCodeRepository.save(promoCode2);
        promoCodeRepository.save(promoCode3);
        promoCodeRepository.save(promoCode4);
        promoCodeRepository.save(promoCode5);
        promoCodeRepository.save(promoCode6);
        promoCodeRepository.save(promoCode7);

    }

    private void seedGiftCards(){
        GiftCard giftCard1 = new GiftCard("Gift Card 1", 10.0, "https://shop.moyu-notebooks.com/cdn/shop/files/moyu-gift-card-10-euros.png?v=1694888807", true);
        GiftCard giftCard2 = new GiftCard("Gift Card 2", 20.0, "https://rideoo.com/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/g/i/gift-card-20-eur-compressor.jpg", true);
        GiftCard giftCard3 = new GiftCard("Gift Card 3", 30.0, "https://shop.moyu-notebooks.com/cdn/shop/files/moyu-gift-card-30-euros.png?v=1701787386&width=600", true);
        GiftCard giftCard4 = new GiftCard("Gift Card 4", 40.0, "https://creativelix.com/wp-content/uploads/2023/10/Gift-Card-40-euro-creativelix-BWLFCREATIVELIX.webp", true);
        GiftCard giftCard5 = new GiftCard("Gift Card 5", 50.0, "https://data.outletmoto.eu/imgprodotto/gift-for-aquisto-of-50-euro_15085_zoom.jpg", true);
        GiftCard giftCard6 = new GiftCard("Gift Card 6", 60.0, "https://www.iris-cayeux.com/5644-large_default/gift-card-60.jpg", true);
        GiftCard giftCard7 = new GiftCard("Gift Card 7", 75.0, "https://by-rofayda.nl/wp-content/uploads/2023/09/75.jpg", true);
        GiftCard giftCard8 = new GiftCard("Gift Card 8", 100.0, "https://by-rofayda.nl/wp-content/uploads/2023/09/100.jpg", true);
        GiftCard giftCard9 = new GiftCard("Gift Card 9", 150.0, "https://by-rofayda.nl/wp-content/uploads/2023/09/150.jpg", true);
        GiftCard giftCard10 = new GiftCard("Gift Card 10", 200.0, "https://danishgallery.com/cdn/shop/products/giftcard200_2048x2048.jpg?v=1547198614", true);
        this.giftCardDAO.createGiftCard(giftCard1);
        this.giftCardDAO.createGiftCard(giftCard2);
        this.giftCardDAO.createGiftCard(giftCard3);
        this.giftCardDAO.createGiftCard(giftCard4);
        this.giftCardDAO.createGiftCard(giftCard5);
        this.giftCardDAO.createGiftCard(giftCard6);
        this.giftCardDAO.createGiftCard(giftCard7);
        this.giftCardDAO.createGiftCard(giftCard8);
        this.giftCardDAO.createGiftCard(giftCard9);
        this.giftCardDAO.createGiftCard(giftCard10);
    }
}
 