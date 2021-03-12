-- MySQL dump 10.13  Distrib 8.0.23, for Win64 (x86_64)
--
-- Host: localhost    Database: library
-- ------------------------------------------------------
-- Server version	8.0.23

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `tbl_books`
--

DROP TABLE IF EXISTS `tbl_books`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_books` (
  `Book_id` int NOT NULL AUTO_INCREMENT,
  `Title` varchar(255) NOT NULL,
  `Author` varchar(255) NOT NULL,
  `Pages` int NOT NULL,
  `Classification` varchar(255) NOT NULL,
  PRIMARY KEY (`Book_id`),
  KEY `book_index` (`Book_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_books`
--

LOCK TABLES `tbl_books` WRITE;
/*!40000 ALTER TABLE `tbl_books` DISABLE KEYS */;
INSERT INTO `tbl_books` VALUES (1,'blomkål','Rudolf Ruskprick',123,'Hce'),(2,'Vägen till Väterås','Kenny Surströmming',244,'Hce'),(3,'Grisarnas julafton','Orvar Satorsson',198,'Hce'),(4,'Blomkålsmördaren','Sara Tryffelsten',55,'uHce'),(5,'Min faster Ingeborg','Inga Skoghorn',763,'Hcf'),(6,'Askorbinsyra utan smör','Tore Tofs',199,'Hcf'),(7,'Lastbilens tankar','Oskar Rudenerg',452,'uHce'),(8,'Benny Bläcks liv','Benny Bläck',111,'Hce');
/*!40000 ALTER TABLE `tbl_books` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_borrowed_books`
--

DROP TABLE IF EXISTS `tbl_borrowed_books`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_borrowed_books` (
  `Borrowed_id` int NOT NULL AUTO_INCREMENT,
  `Book_id` int NOT NULL,
  `Library_card_nr` int NOT NULL,
  PRIMARY KEY (`Borrowed_id`),
  UNIQUE KEY `uc_user_bookId` (`Book_id`),
  KEY `Book_id` (`Book_id`),
  KEY `Library_card_nr` (`Library_card_nr`),
  CONSTRAINT `tbl_borrowed_books_ibfk_1` FOREIGN KEY (`Book_id`) REFERENCES `tbl_books` (`Book_id`) ON DELETE CASCADE,
  CONSTRAINT `tbl_borrowed_books_ibfk_2` FOREIGN KEY (`Library_card_nr`) REFERENCES `tbl_borrowers` (`Library_card_nr`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_borrowed_books`
--

LOCK TABLES `tbl_borrowed_books` WRITE;
/*!40000 ALTER TABLE `tbl_borrowed_books` DISABLE KEYS */;
INSERT INTO `tbl_borrowed_books` VALUES (1,7,3347),(2,8,5432),(3,3,5432);
/*!40000 ALTER TABLE `tbl_borrowed_books` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_borrowers`
--

DROP TABLE IF EXISTS `tbl_borrowers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_borrowers` (
  `Library_card_nr` int NOT NULL,
  `Name` varchar(255) NOT NULL,
  `Adress` varchar(255) DEFAULT NULL,
  `Phone_number` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Library_card_nr`),
  KEY `borrowers_index` (`Library_card_nr`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_borrowers`
--

LOCK TABLES `tbl_borrowers` WRITE;
/*!40000 ALTER TABLE `tbl_borrowers` DISABLE KEYS */;
INSERT INTO `tbl_borrowers` VALUES (1115,'user','Vägen 131, Nollberga','231515'),(1234,'Viggo Filtner','Vägen 1, Nollberga','11111'),(2112,'Elof Öman','Vägen 24, Nollberga','5555'),(3347,'Bosse Baron','Vägen 5, Nollberga','3333'),(4536,'Pelle Pälsänger','Vägen 20, Nollberga','2222'),(5432,'Explorer Johansson','Vägen 123, Nollberga','44444'),(9999,'Jenny Pers','Vägen 111, Nollberga','666677');
/*!40000 ALTER TABLE `tbl_borrowers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_employees`
--

DROP TABLE IF EXISTS `tbl_employees`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_employees` (
  `Employee_id` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(255) NOT NULL,
  `Adress` varchar(255) DEFAULT NULL,
  `Phone_number_1` varchar(255) DEFAULT NULL,
  `Phone_number_2` varchar(255) DEFAULT NULL,
  `Phone_number_3` varchar(255) DEFAULT NULL,
  `Salary` varchar(255) DEFAULT NULL,
  `Vacation_days` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Employee_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_employees`
--

LOCK TABLES `tbl_employees` WRITE;
/*!40000 ALTER TABLE `tbl_employees` DISABLE KEYS */;
INSERT INTO `tbl_employees` VALUES (1,'Asta Kask','Vägen 2, Nollberga','13647','','67869','12000','12'),(2,'Ebba Grön','Vägen 4, Nollberga','365868','6789','','83000','21'),(3,'Farbror Blå','Vägen 8 , Nollberga','68686','','','7000','0');
/*!40000 ALTER TABLE `tbl_employees` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_magazines`
--

DROP TABLE IF EXISTS `tbl_magazines`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_magazines` (
  `Paper_id` int NOT NULL AUTO_INCREMENT,
  `Title` varchar(255) NOT NULL,
  `Release_date` varchar(255) NOT NULL,
  `Location` varchar(255) NOT NULL,
  PRIMARY KEY (`Paper_id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_magazines`
--

LOCK TABLES `tbl_magazines` WRITE;
/*!40000 ALTER TABLE `tbl_magazines` DISABLE KEYS */;
INSERT INTO `tbl_magazines` VALUES (1,'Illustrerad Ångest','2020-12-12','Hylla A'),(2,'Illustrerad Ångest','1985-10-20','Hylla A'),(3,'Illustrerad Ångest','1985-10-11','Hylla A'),(4,'Veckans Tråkigaste','1998-1-1','Hylla A'),(5,'Veckans Tråkigaste','2012-11-5','Hylla A'),(6,'Dagens Tidning','2010-11-11','Hylla B'),(7,'Dagens Tidning','2010-11-10','Hylla B'),(8,'Dagens Tidning','2010-11-9','Hylla B'),(9,'Dagens Tidning','2012-4-5','Hylla B'),(10,'Dagens Tidning','2008-10-5','Hylla B'),(11,'Gårdagens Tidning','1988-10-10','Hylla C'),(12,'Gårdagens Tidning','1975-4-5','Hylla C'),(13,'Gårdagens Tidning','1992-10-10','Hylla C'),(14,'Gårdagens Tidning','1944-2-3','Hylla C'),(15,'Gårdagens Tidning','1957-11-24','Hylla C'),(16,'Gårdagens Tidning','2022-12-1','Hylla C'),(17,'Nyheter från Vattenpölen','2001-6-13','Hylla B'),(18,'Nyheter från Vattenpölen','2003-11-4','Hylla B'),(19,'Nyheter från Vattenpölen','2010-4-13','Hylla B'),(20,'Nyheter från Vattenpölen','2000-1-4','Hylla B'),(21,'Nyheter från Vattenpölen','2015-6-26','Hylla B'),(22,'Moderna trasor','2001-1-5','Hylla A'),(23,'Moderna trasor','2005-8-10','Hylla A'),(24,'Moderna trasor','2017-10-17','Hylla A'),(25,'Moderna trasor','2018-2-2','Hylla A'),(26,'Moderna trasor','2005-8-20','Hylla A'),(27,'Burksamlaren','2012-3-5','Hylla C'),(28,'Burksamlaren','2012-3-7','Hylla C'),(29,'Burksamlaren','2012-3-9','Hylla C');
/*!40000 ALTER TABLE `tbl_magazines` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_users`
--

DROP TABLE IF EXISTS `tbl_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_users` (
  `User_id` int NOT NULL AUTO_INCREMENT,
  `Login_name` varchar(255) NOT NULL,
  `Password` varchar(255) NOT NULL,
  `Is_admin` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`User_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_users`
--

LOCK TABLES `tbl_users` WRITE;
/*!40000 ALTER TABLE `tbl_users` DISABLE KEYS */;
INSERT INTO `tbl_users` VALUES (1,'user','123',0),(2,'admin','123',1);
/*!40000 ALTER TABLE `tbl_users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-03-12 10:54:21
