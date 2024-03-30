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
-- Table structure for table `sales`
--

DROP TABLE IF EXISTS `sales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sales` (
  `sid` int NOT NULL AUTO_INCREMENT,
  `productName` varchar(45) DEFAULT NULL,
  `unitPrice` float(8,2) DEFAULT NULL,
  `quantity` float(8,2) DEFAULT NULL,
  `totalPrice` float(10,2) DEFAULT NULL,
  `discount` float(8,2) DEFAULT NULL,
  `actualPrice` float(9,2) DEFAULT NULL,
  `cashReceived` float(8,2) DEFAULT NULL,
  `dueAmount` float(8,2) DEFAULT NULL,
  `castomerName` varchar(45) DEFAULT NULL,
  `saller` varchar(45) DEFAULT NULL,
  `Date` date DEFAULT NULL,
  PRIMARY KEY (`sid`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales`
--

LOCK TABLES `sales` WRITE;
/*!40000 ALTER TABLE `sales` DISABLE KEYS */;
INSERT INTO `sales` VALUES (1,'Product Name',455.00,25.00,207025.00,45.00,206980.00,15.00,206965.00,'hdf','fdsf',NULL),(2,'Product Name',545.00,75.00,297025.00,4544.00,292481.00,45454.00,247027.00,'dsadfr','gfgrftg',NULL),(3,'Product Name',545.00,75.00,297025.00,4544.00,292481.00,45454.00,247027.00,'dsadfr','gfgrftg',NULL),(4,'Product Name',200.00,45.00,40000.00,450.00,39550.00,4500.00,35050.00,'haasn','Rofiq','2023-09-01'),(5,'Ranii',48.00,44.00,2304.00,48.00,2256.00,485.00,1771.00,'dari','ray','2023-09-02'),(6,'Item 2',5.00,5.00,25.00,5.00,20.05,5.00,15.05,'oi','ujiku','2023-10-11'),(7,'Fan',45.00,745.00,2025.00,57.00,1968.00,75.00,1893.00,'75','754','2023-10-05'),(8,'Rintu',45.00,45.00,2025.00,454.00,1571.00,410.00,1161.00,'411','vhbjh','2023-10-06');
/*!40000 ALTER TABLE `sales` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-10-05  3:24:27
