-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: hasansit
-- ------------------------------------------------------
-- Server version	8.0.34

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `mobile` varchar(45) DEFAULT NULL,
  `address` varchar(70) DEFAULT NULL,
  `dueAmount` float(15,2) DEFAULT '0.00',
  `salesDate` date DEFAULT NULL,
  `receiveDue` float(8,2) DEFAULT NULL,
  `actualDue` float(8,2) DEFAULT NULL,
  `seller` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `mobile_UNIQUE` (`mobile`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (1,'4','4',NULL,409188.00,'2023-10-05',NULL,NULL,'raj'),(2,'Rasel','0179340829',NULL,215.00,'2023-10-05',NULL,NULL,'Hasan'),(3,'Abdullah','017475','Dhaka',0.00,'2023-10-06',NULL,NULL,'Muhammad'),(4,'','4524','745',49494.00,NULL,NULL,NULL,'7577'),(5,'ghg','gh','gh',0.00,'2023-10-05',NULL,NULL,'gh'),(6,'ram','0120','Borosal',547.00,NULL,NULL,NULL,'War'),(7,'Raju','321454','sdf',0.00,'2023-10-04',NULL,NULL,'1Tonmoy'),(8,'Tonmoy','017','Rajbary',5000000.00,'2023-10-01',NULL,NULL,'Hasan');
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `code` varchar(45) DEFAULT NULL,
  `unitPrice` float(8,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'A','',50.00),(95,'C','cc',50.00),(96,'D','dd',4500.00),(97,'E','EE',45.00),(98,'Freze','F-001',45000.00),(99,'AC','Ac-10',48000.00),(100,'Rat','R-45',455.00),(101,'Face','R-45',455.00),(102,'Masud','Y-10',10.00),(103,'Raj','R-4',475.00);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase`
--

DROP TABLE IF EXISTS `purchase`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `code` varchar(45) DEFAULT NULL,
  `unitPrice` float(8,2) DEFAULT NULL,
  `quantity` float(8,2) DEFAULT NULL,
  `totalPrice` float(10,2) DEFAULT NULL,
  `date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase`
--

LOCK TABLES `purchase` WRITE;
/*!40000 ALTER TABLE `purchase` DISABLE KEYS */;
INSERT INTO `purchase` VALUES (1,'A','',50.00,5.00,250.00,'2023-10-05'),(2,'A','',50.00,20.00,1000.00,'2023-10-05'),(3,'C','cc',50.00,10.00,500.00,'2023-10-05'),(4,'C','cc',15.00,5.00,75.00,'2023-10-13'),(5,'Freze','F-001',1500.00,10.00,15000.00,'2023-10-06'),(6,'AC','Ac-10',40500.00,15.00,607500.00,'2023-10-28'),(7,'Raj','R-45',12.00,24.00,288.00,'2023-10-06'),(8,'C','cc',50.00,10.00,500.00,'2023-10-05'),(9,'Freze','F-001',1500.00,100.00,150000.00,'2023-10-06'),(10,'Freze','F-001',1500.00,100.00,150000.00,'2023-10-06'),(11,'C','cc',50.00,10.00,500.00,'2023-10-05'),(12,'Raj','R-45',12.00,24.00,288.00,'2023-10-06'),(13,'A','EE',50.00,5.00,250.00,'2023-10-05'),(14,'AC','Ac-10',40500.00,15.00,607500.00,'2023-10-28'),(15,'AC','Ac-10',40500.00,200.00,8100000.00,'2023-10-28');
/*!40000 ALTER TABLE `purchase` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sales`
--

DROP TABLE IF EXISTS `sales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sales` (
  `sid` int NOT NULL AUTO_INCREMENT,
  `productName` varchar(45) DEFAULT NULL,
  `unitPrice` float(8,2) DEFAULT '0.00',
  `quantity` float(8,2) DEFAULT '0.00',
  `totalPrice` float(10,2) DEFAULT '0.00',
  `discount` float(8,2) DEFAULT '0.00',
  `actualPrice` float(9,2) DEFAULT '0.00',
  `groundTotal` float DEFAULT NULL,
  `cashReceived` float(8,2) DEFAULT '0.00',
  `dueAmount` float(10,2) DEFAULT '0.00',
  `castomerName` varchar(45) DEFAULT NULL,
  `saller` varchar(45) DEFAULT NULL,
  `mobile` varchar(45) DEFAULT NULL,
  `Date` date DEFAULT NULL,
  PRIMARY KEY (`sid`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales`
--

LOCK TABLES `sales` WRITE;
/*!40000 ALTER TABLE `sales` DISABLE KEYS */;
INSERT INTO `sales` VALUES (1,'E',49.50,3.00,148.50,3.00,145.50,145.5,4.00,141.50,'Islam','Raju','0179455',NULL),(2,'Freze',49500.00,4.00,198000.00,4.00,197996.00,197996,5.00,197991.00,'fg','tyhrtf','857',NULL),(3,'Rat',500.50,4.00,2002.00,4.00,1998.00,2143.5,45.00,2098.50,'Islam','Raju','0179455','2023-10-02'),(4,'Freze',49500.00,5.00,247500.00,5.00,247495.00,247495,500.00,246995.00,'Hasan','Raju','01793408295','2023-10-05'),(5,'Freze',49500.00,4.00,198000.00,4.00,197996.00,197996,4.00,197992.00,'a','a','a',NULL),(6,'Freze',49500.00,4.00,198000.00,4.00,197996.00,197996,4.00,197992.00,'a','a','a',NULL),(7,'E',49.50,4.00,198.00,4.00,194.00,194,4.00,190.00,'a','a','01793408295',NULL),(8,'Freze',49500.00,5.00,247500.00,5.00,247495.00,247495,1.00,247494.00,'1','1','017934082',NULL),(9,'Rat',500.50,5.00,2502.50,5.00,2497.50,2497.5,5.00,2492.50,'','','5',NULL),(10,'Freze',49500.00,1.00,49500.00,1.00,49499.00,49499,1.00,49498.00,'1','1','1',NULL),(11,'D',4950.00,5.00,24750.00,5.00,24745.00,24745,1.00,24744.00,'','1','1',NULL),(12,'Face',500.50,1.00,500.50,1.00,499.50,25244.5,1.00,25243.50,'','1','1',NULL),(13,'AC',52800.00,2.00,105600.00,2.00,105598.00,105598,1.00,105597.00,'1','1','1',NULL),(14,'E',49.50,4.00,198.00,4.00,194.00,194,1.00,193.00,'1','1','1',NULL),(15,'AC',52800.00,5.00,264000.00,5.00,263995.00,264015,1.00,264014.00,'gg','1','1',NULL),(16,'Masud',11.00,2.00,22.00,2.00,20.00,264015,1.00,264014.00,'gg','1','1',NULL),(17,'Face',500.50,5.00,2502.50,2.00,2500.50,266516,4.00,266511.50,'Rofiq','Hasan','01793408295',NULL),(18,'Masud',11.00,4.00,44.00,4.00,40.00,266556,1.00,266554.50,'Rofiq','Hasan','01793408295',NULL),(19,'E',49.50,501.00,24799.50,1.00,24798.50,291354,1.00,291353.00,'Rofiq','Hasan','01793408295',NULL),(20,'Freze',49500.00,5.00,247500.00,5.00,247495.00,247495,5.00,247490.00,'7575','7547','25452',NULL),(21,'Freze',49500.00,4.00,198000.00,4.00,197996.00,409192,4.00,409188.00,'4','raj','4','2023-10-05'),(22,'AC',52800.00,4.00,211200.00,4.00,211196.00,409192,4.00,409188.00,'4','raj','4','2023-10-05'),(23,'C',55.00,4.00,220.00,4.00,216.00,216,1.00,215.00,'Rasel','Hasan','0179340829','2023-10-05'),(24,'E',49.50,4.00,198.00,4.00,194.00,410,5.00,405.00,'Abdullah','Muhammad','017475','2023-10-06'),(25,'C',55.00,4.00,220.00,4.00,216.00,410,5.00,405.00,'Abdullah','Muhammad','017475','2023-10-06'),(26,'Freze',49500.00,1.00,49500.00,1.00,49499.00,49499,5.00,49494.00,'','7577','4524',NULL),(27,'D',4950.00,4.00,19800.00,4.00,19796.00,19796,1.00,19795.00,'ghg','gh','gh','2023-10-05'),(28,'C',55.00,10.00,550.00,1.00,549.00,549,2.00,547.00,'ram','War','0120',NULL),(29,'C',55.00,3.00,165.00,1.00,164.00,164,4.00,160.00,'Raju','1Tonmoy','321454','2023-10-04'),(30,'Freze',49500.00,1.00,49500.00,1.00,49499.00,49499,1.00,49498.00,'Tonmoy','Hasan','017','2023-10-01'),(31,'Freze',49500.00,82.00,4059000.00,100000.00,3959000.00,3959000,100000.00,3859000.00,'Tonmoy','Hasan','017',NULL),(32,'AC',52800.00,50.00,2640000.00,100000.00,2540000.00,2540000,1.00,2539999.00,'tonmoy','Hasan','017',NULL);
/*!40000 ALTER TABLE `sales` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stock`
--

DROP TABLE IF EXISTS `stock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stock` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `code` varchar(45) DEFAULT NULL,
  `unitPrice` float(8,2) DEFAULT NULL,
  `quantity` float(5,2) DEFAULT NULL,
  `totalPrice` float(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stock`
--

LOCK TABLES `stock` WRITE;
/*!40000 ALTER TABLE `stock` DISABLE KEYS */;
INSERT INTO `stock` VALUES (1,'A','',50.00,-81.00,NULL),(3,'C','cc',50.00,10.00,NULL),(4,'D','dd',4500.00,-23.00,NULL),(5,'E','EE',45.00,-538.00,NULL),(6,'Freze','F-001',45000.00,0.00,NULL),(7,'AC','Ac-10',48000.00,120.00,NULL),(8,'Rat','R-45',455.00,-34.00,NULL),(9,'Face','R-45',455.00,-10.00,NULL),(10,'Masud','Y-10',10.00,-6.00,NULL),(11,'Raj','R-4',475.00,25.00,NULL);
/*!40000 ALTER TABLE `stock` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-10-17  8:29:53
