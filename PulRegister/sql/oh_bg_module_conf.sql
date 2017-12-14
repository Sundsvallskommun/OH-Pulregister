INSERT INTO `openhierarchy_foreground_modules` ( `classname`, `name`, `alias`, `description`, `xslPath`, `xslPathType`, `anonymousAccess`, `userAccess`, `adminAccess`, `enabled`, `visibleInMenu`, `sectionID`, `dataSourceID`, `staticContentPackage`, `requiredProtocol`) VALUES ('se.sundsvallskommun.nodes.PulRegistryModule', 'pulregister', 'nodes', 'PUL-Register', 'NodesTemplate.xsl', 'Classpath', 1, 1, 1, 1, 0, 3, NULL, 'staticcontent', NULL);
INSERT INTO `openhierarchy_foreground_modules` ( `classname`, `name`, `alias`, `description`, `xslPath`, `xslPathType`, `anonymousAccess`, `userAccess`, `adminAccess`, `enabled`, `visibleInMenu`, `sectionID`, `dataSourceID`, `staticContentPackage`, `requiredProtocol`) VALUES ('se.unlogic.hierarchy.foregroundmodules.rest.AnnotatedRESTModule', 'Rest', 'rest', 'rest', NULL, NULL, 0, 0, 1, 1, 0, 7, NULL, NULL, NULL);

DROP TABLE IF EXISTS `openhierarchy_sections`;
CREATE TABLE IF NOT EXISTS `openhierarchy_sections` (
  `sectionID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `parentSectionID` int(10) unsigned DEFAULT NULL,
  `alias` varchar(255) NOT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT '0',
  `anonymousAccess` tinyint(1) NOT NULL DEFAULT '0',
  `userAccess` tinyint(1) NOT NULL DEFAULT '0',
  `adminAccess` tinyint(1) NOT NULL DEFAULT '0',
  `visibleInMenu` tinyint(1) NOT NULL DEFAULT '0',
  `breadCrumb` tinyint(1) DEFAULT '1',
  `name` varchar(255) NOT NULL DEFAULT '',
  `description` varchar(255) NOT NULL DEFAULT '',
  `anonymousDefaultURI` varchar(255) DEFAULT NULL,
  `userDefaultURI` varchar(255) DEFAULT NULL,
  `requiredProtocol` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`sectionID`),
  UNIQUE KEY `Index_2` (`parentSectionID`,`alias`),
  CONSTRAINT `FK_sections_1` FOREIGN KEY (`parentSectionID`) REFERENCES `openhierarchy_sections` (`sectionID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

INSERT INTO `openhierarchy_sections` (`sectionID`, `parentSectionID`, `alias`, `enabled`, `anonymousAccess`, `userAccess`, `adminAccess`, `visibleInMenu`, `breadCrumb`, `name`, `description`, `anonymousDefaultURI`, `userDefaultURI`, `requiredProtocol`) VALUES
	(1, NULL, 'start', 1, 1, 1, 1, 1, 1, 'Startsidan', 'Startsidan', '/pulregister/nodes/showflatlist', '/pulregister/nodes/showflatlist', NULL),
	(3, 1, 'pulregister', 1, 1, 1, 1, 1, 1, 'PUL-Register', 'PUL-Register', '/pulregister/showflatlist', '/pulregister/showflatlist', NULL),
	(7, 1, 'administration', 1, 0, 1, 1, 1, 1, 'Administration', 'Administration och övervakning', '/systemadmin', '/systemadmin', NULL);
