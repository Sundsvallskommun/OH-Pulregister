UPDATE `register.sundsvall.se`.`node_template_attributes` SET `required`='1' WHERE `template_attribute_id`='1';

INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value_type`, `template_attribute_id`, `description`, `parent_template_attribute`, `show_only_when_value_equals`) VALUES ('1', '', 'STRINGBOX', '39', 'Vart och hur sparas samtycket?', '16', '0');

INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value`, `value_type`, `template_attribute_id`, `description`) VALUES ('1', 'Fritext', 'Nej|Ja', 'SINGLESELECT', '40', 'Finns det plats för fritext?');

INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value`, `value_type`, `template_attribute_id`, `description`, `parent_template_attribute`, `show_only_when_value_equals`) VALUES ('1', '', '', 'STRINGBOX', '41', 'Vad skrivs i fritexten och varför?', '40', '1');

INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value`, `value_type`, `template_attribute_id`, `description`) VALUES ('1', 'Datakällor', 'Nej|Ja', 'SINGLESELECT', '42', 'Föds systemet med personuppgifter från andra datakällor eller föder systemet andra system med personuppgifter?');

INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value`, `value_type`, `template_attribute_id`, `description`, `parent_template_attribute`, `show_only_when_value_equals`) VALUES ('1', ' ', '', 'STRINGBOX', '43', 'Vilka system är födare av eller föds med personuppgifter?', '42', '1');

UPDATE `register.sundsvall.se`.`node_template_attributes` SET `value`='Namn|Adress|Fastighetsbeteckning|Epost|Personnummer|Lön på personnivå|Telefonnummer|Mobilnummer|ip-nummer|Kundnummer|GPS-kordinat kopplat till en individ|Nej, men genom kompletterande uppgifter kan man enkelt identifiera fysiska personer t.ex. utgallring, single out.|Annat' WHERE `template_attribute_id`='4';

INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value`, `value_type`, `template_attribute_id`, `description`, `required`, `parent_template_attribute`, `show_only_when_value_equals`) VALUES ('1', 'Nej, men genom kompletterande uppgifter kan man enkelt identifiera fysiska personer t.ex. utgallring, single out.', '', 'STRINGBOX', '44', 'Vilka personuppgifter kan finnas om man lyckas med utgallring?', '0', '4', '11');

INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value`, `value_type`, `template_attribute_id`, `description`, `parent_template_attribute`, `show_only_when_value_equals`) VALUES ('1', 'Annat', '', 'STRINGBOX', '45', 'Beskriv typ(er) av personuppgift mer utförligt. Det kan t ex vara så att allt data är pseudonymiserat och därigenom inte innehåller några av de "klassiska" personuppgifterna, men genom att ha koll på nyckeln så kan man identifiera personuppgifter såsom mängd avfall som slängs.', '4', '12');

INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value`, `value_type`, `template_attribute_id`, `description`) VALUES ('1', 'Typ av register', 'IT-system|Annat ostrukturerat material', 'SINGLESELECT', '46', 'Vilken typ av lagringsvariant sker behandlingen i?');

INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value`, `value_type`, `template_attribute_id`, `description`, `parent_template_attribute`, `show_only_when_value_equals`) VALUES ('1', '', '', 'STRINGBOX', '47', 'Bekriv typ av register i mer detalj.', '46', '1');

INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value`, `value_type`, `template_attribute_id`, `description`, `required`, `parent_template_attribute`, `show_only_when_value_equals`) VALUES ('1', '', '', 'STRINGBOX', '48', 'Kommentera på dem känsliga personuppgifter som registret innehåller.', '0', '5', 'true');

INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value`, `value_type`, `template_attribute_id`, `parent_template_attribute`, `show_only_when_value_equals`) VALUES ('1', 'Företag', '', 'STRING', '49', '11', '2');
INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value`, `value_type`, `template_attribute_id`, `parent_template_attribute`, `show_only_when_value_equals`) VALUES ('1', 'Ansvarig', '', 'STRING', '50', '11', '2');
INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value`, `value_type`, `template_attribute_id`, `parent_template_attribute`, `show_only_when_value_equals`) VALUES ('1', 'Telefon', '', 'STRING', '51', '11', '2');
INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value_type`, `template_attribute_id`, `parent_template_attribute`, `show_only_when_value_equals`) VALUES ('1', 'E-post', 'STRING', '52', '11', '2');

INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value_type`, `template_attribute_id`, `description`, `parent_template_attribute`, `show_only_when_value_equals`) VALUES ('1', 'Personuppgifterna samlas in från någon annan', 'STRINGBOX', '53', 'Vem samlas personuppgifter in från?', '19', '1');

INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value_type`, `template_attribute_id`) VALUES ('1', 'Allmänna anteckningar', 'SINGLESELECT', '54');
INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value`, `value_type`, `template_attribute_id`, `description`, `required`, `parent_template_attribute`) VALUES ('1', 'Medverkande i dokumentation av behandling', '', 'STRING', '55', '', '0', '54');
INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value_type`, `template_attribute_id`, `required`, `parent_template_attribute`) VALUES ('1', 'Kommentar rörande dokumentation av behandling', 'STRINGBOX', '56', '0', '54');

UPDATE `register.sundsvall.se`.`node_template_attributes` SET `name`='Sakkunnig i verksamheten' WHERE `template_attribute_id`='37';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `name`='E-post (Sakkunnig i verksamheten)' WHERE `template_attribute_id`='38';
INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value_type`, `template_attribute_id`, `parent_template_attribute`) VALUES ('1', 'Telefonnummer (Sakkunning i verksamheten)', 'STRING', '57', '37');
INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value_type`, `template_attribute_id`, `parent_template_attribute`) VALUES ('1', 'Systemägare', 'STRING', '58', '37');
INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value_type`, `template_attribute_id`, `parent_template_attribute`) VALUES ('1', 'E-post (Systemägare)', 'STRING', '59', '37');
INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value_type`, `template_attribute_id`, `parent_template_attribute`) VALUES ('1', 'Telefonnummer (Systemägare)', 'STRING', '60', '37');
INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value_type`, `template_attribute_id`, `parent_template_attribute`) VALUES ('1', 'Systemförvaltare', 'STRING', '61', '37');
INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value_type`, `template_attribute_id`, `parent_template_attribute`) VALUES ('1', 'E-post (Systemförvaltare)', 'STRING', '62', '37');
INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value_type`, `template_attribute_id`, `parent_template_attribute`) VALUES ('1', 'Telefonnummer (Systemförvaltare)', 'STRING', '63', '37');

INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value`, `value_type`, `template_attribute_id`) VALUES ('1', 'Status', 'Ej påbörjad|Påbörjad|Klar', 'ENUM', '64');

ALTER TABLE `register.sundsvall.se`.`node_attributes` 
ADD COLUMN `required_action` TINYINT(1) NULL AFTER `template_id`;

