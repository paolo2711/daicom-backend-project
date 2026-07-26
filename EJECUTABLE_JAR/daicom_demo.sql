-- MariaDB dump 10.19  Distrib 10.4.32-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: daicom_demo
-- ------------------------------------------------------
-- Server version	10.4.32-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `daicom_demo`
--

/*!40000 DROP DATABASE IF EXISTS `daicom_demo`*/;

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `daicom_demo` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;

USE `daicom_demo`;

--
-- Table structure for table `certificates`
--

DROP TABLE IF EXISTS `certificates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `certificates` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `attached_pdf` varchar(255) DEFAULT NULL,
  `certificate_type` enum('ACREDITADO','NO_ACREDITADO','OPERATIVIDAD') NOT NULL,
  `correlative` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `emission_date` date NOT NULL,
  `equipment` varchar(255) NOT NULL,
  `signature_requested` bit(1) NOT NULL,
  `status` int(11) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `uploaded_xls` varchar(255) DEFAULT NULL,
  `uuid` varchar(36) DEFAULT NULL,
  `client_id` bigint(20) NOT NULL,
  `company_id` bigint(20) NOT NULL,
  `lab_id` bigint(20) NOT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKlnwjsut7480baufv45j0i4bf3` (`client_id`),
  KEY `FK2ysnvm0pp9vustybk40r6dpn4` (`company_id`),
  KEY `FKk1kkqwa88rjf82dfr6cra5x9d` (`lab_id`),
  KEY `FKbb1kd8b4klj4w2ft3pns2h87o` (`order_id`),
  CONSTRAINT `FK2ysnvm0pp9vustybk40r6dpn4` FOREIGN KEY (`company_id`) REFERENCES `companies` (`id`),
  CONSTRAINT `FKbb1kd8b4klj4w2ft3pns2h87o` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `FKk1kkqwa88rjf82dfr6cra5x9d` FOREIGN KEY (`lab_id`) REFERENCES `labs` (`id`),
  CONSTRAINT `FKlnwjsut7480baufv45j0i4bf3` FOREIGN KEY (`client_id`) REFERENCES `clients` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `certificates`
--

LOCK TABLES `certificates` WRITE;
/*!40000 ALTER TABLE `certificates` DISABLE KEYS */;
INSERT INTO `certificates` VALUES (1,'','ACREDITADO',4,'2026-07-12 03:09:09.000000','2026-07-12','PRUEBA','\0',1,'2026-07-21 00:37:28.000000','','5e42965f-3cd8-4ed0-a8ff-f94b9c0a9e64',2,1,1,1),(2,'','ACREDITADO',5,'2026-07-18 02:11:02.000000','2026-07-18','catar','\0',1,'2026-07-18 02:11:02.000000','','43df441a-8a75-44c9-a4e8-009c616cd3a1',2,1,1,3),(3,'','ACREDITADO',6,'2026-07-21 00:19:31.000000','2026-07-21','ejemplo','\0',1,'2026-07-21 00:19:31.000000','','4e81e168-5ce8-4946-b11d-b29aedd1e823',4,1,1,4),(4,'signed/803c421c-5d56-4103-95db-7b41a8c89e9e_qr.pdf','ACREDITADO',7,'2026-07-21 00:26:10.000000','2026-07-21','TEST REFRESH UI EDITADO','\0',1,'2026-07-21 02:31:39.000000','bases/5adb71c4-c109-47bb-82f8-63931604e23e_2026-07-16_ALQ_2026_00003_F_fa596d.pdf','57de1f7d-6fd4-47a2-a87f-ed4874912e70',3,1,1,NULL),(5,'signed/74bace54-7388-4605-a9e0-799cf898082d_qr.pdf','ACREDITADO',8,'2026-07-21 00:37:54.000000','2026-07-21','ejmp','\0',1,'2026-07-21 02:27:08.000000','bases/856b7b57-ed18-44d2-a5a4-df9f7c596b77_base.pdf','911031e4-05bd-4836-9300-8c53084931c8',4,1,1,4),(6,'','ACREDITADO',9,'2026-07-21 01:37:17.000000','2026-07-21','EQUIPO EXTRA NUEVO TEST','\0',1,'2026-07-21 02:36:36.000000','bases/1278f4c0-5304-42f2-b357-a10f6c8461e6_2026-07-16_OS_2026_00219_F_486845.pdf','b4eeba57-9d7a-470c-ba5b-0bcac06d26c2',2,1,1,3),(7,'','ACREDITADO',10,'2026-07-21 01:42:00.000000','2026-07-21','prueba','\0',1,'2026-07-21 01:42:00.000000','','d86ba906-add3-45de-8c59-c537dea5e256',2,1,3,2);
/*!40000 ALTER TABLE `certificates` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clients`
--

DROP TABLE IF EXISTS `clients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clients` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `document` varchar(255) NOT NULL,
  `document_type` enum('DNI','NO_DOCUMENT','RUC') NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `company_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKs3vg3lr3eadee5luiq6c78t1i` (`company_id`),
  CONSTRAINT `FKs3vg3lr3eadee5luiq6c78t1i` FOREIGN KEY (`company_id`) REFERENCES `companies` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clients`
--

LOCK TABLES `clients` WRITE;
/*!40000 ALTER TABLE `clients` DISABLE KEYS */;
INSERT INTO `clients` VALUES (1,'','','12345345','RUC','','R','',1),(2,'','\0','70971511','DNI','','PAOLO RODRIGO RODRIGUEZ PAREDES','',1),(3,'','\0','29648371','DNI','','AMELIA ELSA PAREDES CANO','',1),(4,'CAL. OCTAVIO MUÑOZ NAJAR NRO 109 INT. 111 URB. CERCADO , AREQUIPA, AREQUIPA','\0','20601026211','NO_DOCUMENT','','METROLOGICA IMPORT EXPORT S.A.C.','',1),(5,'','\0','','NO_DOCUMENT','','CLIENTE SIN DOC TEST','',1),(6,'','\0','12345678','DNI','','CLIENTE DNI TEST','',1);
/*!40000 ALTER TABLE `clients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `companies`
--

DROP TABLE IF EXISTS `companies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `companies` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `accredited_correlative` bigint(20) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `non_accredited_correlative` bigint(20) NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `operational_correlative` bigint(20) NOT NULL,
  `service_order_correlative` bigint(20) NOT NULL,
  `document` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `companies`
--

LOCK TABLES `companies` WRITE;
/*!40000 ALTER TABLE `companies` DISABLE KEYS */;
INSERT INTO `companies` VALUES (1,11,'Av. Principal 123, Arequipa','contacto@daicom.com.pe','DAICOM S.A.C.',1,'054-123456',7,5,'');
/*!40000 ALTER TABLE `companies` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `labs`
--

DROP TABLE IF EXISTS `labs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `labs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `company_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKlfu7lbuny1iu963fvyst98n2e` (`code`),
  KEY `FK1i9txuh2qckwt4ax1qtb5366` (`company_id`),
  CONSTRAINT `FK1i9txuh2qckwt4ax1qtb5366` FOREIGN KEY (`company_id`) REFERENCES `companies` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `labs`
--

LOCK TABLES `labs` WRITE;
/*!40000 ALTER TABLE `labs` DISABLE KEYS */;
INSERT INTO `labs` VALUES (1,'F','Fuerza',1),(3,'LLT','LAB LOG TEST',1);
/*!40000 ALTER TABLE `labs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `log_entries`
--

DROP TABLE IF EXISTS `log_entries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `log_entries` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `action` varchar(255) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `entity_affected` varchar(255) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKr7l216df4fsipce8vsqurbff4` (`user_id`),
  CONSTRAINT `FKr7l216df4fsipce8vsqurbff4` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log_entries`
--

LOCK TABLES `log_entries` WRITE;
/*!40000 ALTER TABLE `log_entries` DISABLE KEYS */;
INSERT INTO `log_entries` VALUES (1,'CREATE_CERTIFICATE','2026-07-12 00:58:24.000000','Certificate ID: 1',1),(2,'CREATE_CERTIFICATE','2026-07-12 01:29:00.000000','Certificate ID: 1',1),(3,'CREATE_CERTIFICATE','2026-07-12 01:30:09.000000','Certificate ID: 2',1),(4,'CREATE_ORDER','2026-07-12 03:06:03.000000','Order Number: OS-2026-00001',1),(5,'CREATE_ORDER','2026-07-12 03:09:09.000000','Order Number: OS-2026-00002',1),(6,'CREATE_ORDER','2026-07-18 02:11:02.000000','Order Number: OS-2026-00003',1),(7,'CREATE_ORDER','2026-07-21 00:19:31.000000','Order Number: OS-2026-00004',1),(8,'CREATE_CERTIFICATE','2026-07-21 00:26:10.000000','Certificate ID: 4',1),(9,'CREATE_CERTIFICATE','2026-07-21 00:37:54.000000','Certificate ID: 5',1),(10,'CREATE_LAB','2026-07-21 00:43:07.000000','Lab: LAB LOG TEST',1),(11,'CREATE_CLIENT','2026-07-21 00:48:42.000000','Client: CLIENTE SIN DOC TEST',1),(12,'CREATE_CLIENT','2026-07-21 00:49:37.000000','Client: CLIENTE DNI TEST',1),(13,'CREATE_CERTIFICATE','2026-07-21 01:37:17.000000','Certificate ID: 6',1),(14,'CREATE_CERTIFICATE','2026-07-21 01:42:00.000000','Certificate ID: 7',1);
/*!40000 ALTER TABLE `log_entries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_invoices`
--

DROP TABLE IF EXISTS `order_invoices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_invoices` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` decimal(10,2) NOT NULL,
  `invoice_date` date DEFAULT NULL,
  `invoice_number` varchar(255) DEFAULT NULL,
  `pdf` varchar(255) DEFAULT NULL,
  `order_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKp378jl7fuclx1xyh7mnjgoja6` (`order_id`),
  CONSTRAINT `FKp378jl7fuclx1xyh7mnjgoja6` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_invoices`
--

LOCK TABLES `order_invoices` WRITE;
/*!40000 ALTER TABLE `order_invoices` DISABLE KEYS */;
INSERT INTO `order_invoices` VALUES (1,1300.00,NULL,NULL,NULL,1),(2,500.00,'2026-07-15','F001-00999',NULL,1);
/*!40000 ALTER TABLE `order_invoices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_payments`
--

DROP TABLE IF EXISTS `order_payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_payments` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` decimal(10,2) NOT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `payment_date` date NOT NULL,
  `payment_method` enum('BILLETERA','EFECTIVO','TRANSFERENCIA') NOT NULL,
  `payment_proof` varchar(255) DEFAULT NULL,
  `order_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3s9vxneb3dk3plhpv9s213so0` (`order_id`),
  CONSTRAINT `FK3s9vxneb3dk3plhpv9s213so0` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_payments`
--

LOCK TABLES `order_payments` WRITE;
/*!40000 ALTER TABLE `order_payments` DISABLE KEYS */;
INSERT INTO `order_payments` VALUES (1,300.00,'Abono de prueba','2026-07-18','TRANSFERENCIA',NULL,1);
/*!40000 ALTER TABLE `order_payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orders` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `order_number` varchar(255) NOT NULL,
  `order_type` int(11) NOT NULL,
  `sent` bit(1) NOT NULL,
  `status` int(11) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `wants_invoice` bit(1) NOT NULL,
  `client_id` bigint(20) NOT NULL,
  `company_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKnthkiu7pgmnqnu86i2jyoe2v7` (`order_number`),
  KEY `FKm2dep9derpoaehshbkkatam3v` (`client_id`),
  KEY `FK1vldikbqexeu85qvsedncxvs3` (`company_id`),
  CONSTRAINT `FK1vldikbqexeu85qvsedncxvs3` FOREIGN KEY (`company_id`) REFERENCES `companies` (`id`),
  CONSTRAINT `FKm2dep9derpoaehshbkkatam3v` FOREIGN KEY (`client_id`) REFERENCES `clients` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,'2026-07-12 03:06:03.000000','\0','OS-2026-00001',1,'\0',2,'2026-07-21 00:37:28.000000','',2,1),(2,'2026-07-12 03:09:09.000000','\0','OS-2026-00002',1,'\0',1,'2026-07-12 03:09:09.000000','',2,1),(3,'2026-07-18 02:11:02.000000','\0','OS-2026-00003',1,'\0',1,'2026-07-18 02:11:02.000000','',2,1),(4,'2026-07-21 00:19:31.000000','\0','OS-2026-00004',1,'\0',1,'2026-07-21 00:19:31.000000','',4,1);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_permissions`
--

DROP TABLE IF EXISTS `role_permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_permissions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `endpoint` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `permission_code` int(11) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKn5fotdgk8d1xvo8nav9uv3muc` (`role_id`),
  CONSTRAINT `FKn5fotdgk8d1xvo8nav9uv3muc` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_permissions`
--

LOCK TABLES `role_permissions` WRITE;
/*!40000 ALTER TABLE `role_permissions` DISABLE KEYS */;
INSERT INTO `role_permissions` VALUES (12,'/home','Inicio',1,1),(13,'/orders','Órdenes de Servicio',2,1),(14,'/certificates','Certificados',5,1),(15,'/labs','Laboratorios',6,1),(16,'/clients','Clientes',7,1),(17,'FIRMAR_QR','Firmar / Generar QR',10,1),(18,'VER_RESUMEN_ORDEN','Ver Resumen de Orden',11,1),(19,'ANULAR_CERTIFICADO','Anular / Restaurar Certificados',12,1),(20,'ANULAR_ORDEN','Anular Órdenes',13,1),(21,'SOLICITAR_FIRMA','Solicitar Firma',14,1),(22,'/company','Datos de la Empresa',113,1),(23,'/permissions','Permisos',112,1),(24,'/users','Usuarios',111,1),(28,'VER_RESUMEN_ORDEN','Ver Resumen de Orden',11,3),(29,'SOLICITAR_FIRMA','Solicitar Firma',14,3),(30,'/company','Datos de la Empresa',113,3),(31,'/clients','Clientes',7,3),(32,'/labs','Laboratorios',6,3),(33,'/certificates','Certificados',5,3),(34,'/orders','Órdenes de Servicio',2,3),(35,'/home','Inicio',1,3),(36,'/users','Usuarios',111,3),(40,'/certificates','Certificados',5,2),(41,'FIRMAR_QR','Firmar / Generar QR',10,2),(42,'/users','Usuarios',111,2);
/*!40000 ALTER TABLE `role_permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `roles` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `admin` bit(1) NOT NULL,
  `name` varchar(255) NOT NULL,
  `company_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKcws3108ier5wt4b0df1l5ytcm` (`company_id`),
  CONSTRAINT `FKcws3108ier5wt4b0df1l5ytcm` FOREIGN KEY (`company_id`) REFERENCES `companies` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'','Administrador',1),(2,'\0','Metrologo',1),(3,'\0','Recepcion',1);
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('ADMIN','USER') NOT NULL,
  `username` varchar(255) NOT NULL,
  `company_id` bigint(20) NOT NULL,
  `role_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
  UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`),
  KEY `FKin8gn4o1hpiwe6qe4ey7ykwq7` (`company_id`),
  KEY `FKp56c1712k691lhsyewcssf40f` (`role_id`),
  CONSTRAINT `FKin8gn4o1hpiwe6qe4ey7ykwq7` FOREIGN KEY (`company_id`) REFERENCES `companies` (`id`),
  CONSTRAINT `FKp56c1712k691lhsyewcssf40f` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin@daicom.com','Paolo','Rodrig','$2a$10$5e2HQROfJnMAH9n9GRzbc.1ULS01PJfFWEZT8LV.DfgT787Fxw94O','ADMIN','admin1234',1,1),(2,'','Juan','Perez','$2a$10$lPNalX4XrCCUYcLH3Y5xPefD4xHJUEtq5UpUah9V2lHpedxsWzaVq','USER','metrologo1',1,2);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'daicom_demo'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-07-25 19:55:40
