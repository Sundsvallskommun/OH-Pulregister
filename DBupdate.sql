UPDATE `register.sundsvall.se`.`node_template_attributes` SET `required`='1' WHERE `template_attribute_id`='2';

INSERT INTO `register.sundsvall.se`.`node_attribute_types` (`attribute_type_name`) VALUES ('SPECIALSTRING');

INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value_type`, `template_attribute_id`, `description`, `parent_template_attribute`, `show_only_when_value_equals`) VALUES ('1', '', 'STRINGBOX', '39', 'Vart och hur sparas samtycket?', '16', '0');

INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value`, `value_type`, `template_attribute_id`, `description`) VALUES ('1', 'Finns det plats för fritext?', 'Nej|Ja', 'SINGLESELECT', '40', '');

INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value`, `value_type`, `template_attribute_id`, `description`, `parent_template_attribute`, `show_only_when_value_equals`) VALUES ('1', '', '', 'STRINGBOX', '41', 'Vad finns i fritexten och varför? Finns det rutiner för hur fritext får skrivas?', '40', '1');

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


INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value`, `value_type`, `template_attribute_id`, `description`, `required`) VALUES ('1', 'Medverkande i dokumentation av behandling', '', 'STRING', '55', '', '0');
INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value_type`, `template_attribute_id`, `required`, `parent_template_attribute`) VALUES ('1', 'Kommentar rörande dokumentation av behandling', 'STRINGBOX', '56', '0', '55');

UPDATE `register.sundsvall.se`.`node_template_attributes` SET `name`='Sakkunnig i verksamheten' WHERE `template_attribute_id`='37';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `value_type`='SPECIALSTRING' WHERE `template_attribute_id`='37';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `name`='E-post (Sakkunnig i verksamheten)' WHERE `template_attribute_id`='38';
INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value_type`, `template_attribute_id`, `parent_template_attribute`) VALUES ('1', 'Telefonnummer (Sakkunning i verksamheten)', 'STRING', '57', '37');
INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value_type`, `template_attribute_id`, `parent_template_attribute`) VALUES ('1', 'Systemägare', 'STRING', '58', '37');
INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value_type`, `template_attribute_id`, `parent_template_attribute`) VALUES ('1', 'E-post (Systemägare)', 'STRING', '59', '37');
INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value_type`, `template_attribute_id`, `parent_template_attribute`) VALUES ('1', 'Telefonnummer (Systemägare)', 'STRING', '60', '37');
INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value_type`, `template_attribute_id`, `parent_template_attribute`) VALUES ('1', 'Systemförvaltare', 'STRING', '61', '37');
INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value_type`, `template_attribute_id`, `parent_template_attribute`) VALUES ('1', 'E-post (Systemförvaltare)', 'STRING', '62', '37');
INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value_type`, `template_attribute_id`, `parent_template_attribute`) VALUES ('1', 'Telefonnummer (Systemförvaltare)', 'STRING', '63', '37');

INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value`, `value_type`, `template_attribute_id`) VALUES ('1', 'Status', 'Ej påbörjad|Påbörjad|Klar', 'ENUM', '64');

UPDATE `register.sundsvall.se`.`node_template_attributes` SET `value_type`='ENUM' WHERE `template_attribute_id`='2';

UPDATE `register.sundsvall.se`.`node_template_attributes` SET `name`='Kontaktperson', `description`='Kontaktpersonen är den som är sakkunnig i verksamheten om den aktuella behandlingen, denna person är den som kontaktas vid frågor rörande behandling eller vid begäran av registerutdrag.' WHERE `template_attribute_id`='37';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `name`='E-post (Kontaktperson)', `value`='' WHERE `template_attribute_id`='38';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `name`='Telefonnummer (Kontaktperson)' WHERE `template_attribute_id`='57';

INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value`, `value_type`, `template_attribute_id`, `description`, `parent_template_attribute`,`show_only_when_value_equals`) VALUES ('1', '', '', 'STRINGBOX', '65', 'Bekriv hur de registrerade informeras i mer detalj.', '20','0|1|2');

ALTER TABLE `register.sundsvall.se`.`node_template_attributes` 
ADD COLUMN `group_ids` VARCHAR(1024) NULL AFTER `show_only_when_value_equals`;

INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value`, `value_type`, `template_attribute_id`, `parent_template_attribute`) VALUES ('1', '', 'Åtgärder krävs', 'MULTISELECT', '101', '4');

INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value`, `value_type`, `template_attribute_id`, `parent_template_attribute`) VALUES ('1', '', 'Åtgärder krävs', 'MULTISELECT', '102', '5');

INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value`, `value_type`, `template_attribute_id`, `parent_template_attribute`) VALUES ('1', '', 'Åtgärder krävs', 'MULTISELECT', '103', '6');

INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value`, `value_type`, `template_attribute_id`, `parent_template_attribute`) VALUES ('1', '', 'Åtgärder krävs', 'MULTISELECT', '104', '7');

INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value`, `value_type`, `template_attribute_id`, `parent_template_attribute`) VALUES ('1', '', 'Åtgärder krävs', 'MULTISELECT', '105', '8');

INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value`, `value_type`, `template_attribute_id`, `parent_template_attribute`) VALUES ('1', '', 'Åtgärder krävs', 'MULTISELECT', '106', '9');

INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value`, `value_type`, `template_attribute_id`, `parent_template_attribute`) VALUES ('1', '', 'Åtgärder krävs', 'MULTISELECT', '107', '10');

INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value`, `value_type`, `template_attribute_id`, `parent_template_attribute`) VALUES ('1', '', 'Åtgärder krävs', 'MULTISELECT', '108', '11');

INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value`, `value_type`, `template_attribute_id`, `parent_template_attribute`) VALUES ('1', '', 'Åtgärder krävs', 'MULTISELECT', '109', '15');

INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value`, `value_type`, `template_attribute_id`, `parent_template_attribute`) VALUES ('1', '', 'Åtgärder krävs', 'MULTISELECT', '110', '16');

INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value`, `value_type`, `template_attribute_id`, `parent_template_attribute`) VALUES ('1', '', 'Åtgärder krävs', 'MULTISELECT', '111', '17');

INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value`, `value_type`, `template_attribute_id`, `parent_template_attribute`) VALUES ('1', '', 'Åtgärder krävs', 'MULTISELECT', '112', '19');

INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value`, `value_type`, `template_attribute_id`, `parent_template_attribute`) VALUES ('1', '', 'Åtgärder krävs', 'MULTISELECT', '113', '20');

INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value`, `value_type`, `template_attribute_id`, `parent_template_attribute`) VALUES ('1', '', 'Åtgärder krävs', 'MULTISELECT', '114', '21');

INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value`, `value_type`, `template_attribute_id`, `parent_template_attribute`) VALUES ('1', '', 'Åtgärder krävs', 'MULTISELECT', '115', '23');

INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value`, `value_type`, `template_attribute_id`, `parent_template_attribute`) VALUES ('1', '', 'Åtgärder krävs', 'MULTISELECT', '116', '25');

INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value`, `value_type`, `template_attribute_id`, `parent_template_attribute`) VALUES ('1', '', 'Åtgärder krävs', 'MULTISELECT', '117', '30');

INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value`, `value_type`, `template_attribute_id`, `parent_template_attribute`) VALUES ('1', '', 'Åtgärder krävs', 'MULTISELECT', '118', '37');

INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value`, `value_type`, `template_attribute_id`, `parent_template_attribute`) VALUES ('1', '', 'Åtgärder krävs', 'MULTISELECT', '119', '40');

INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value`, `value_type`, `template_attribute_id`, `parent_template_attribute`) VALUES ('1', '', 'Åtgärder krävs', 'MULTISELECT', '120', '42');

INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value`, `value_type`, `template_attribute_id`, `parent_template_attribute`) VALUES ('1', '', 'Åtgärder krävs', 'MULTISELECT', '121', '46');

INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value`, `value_type`, `template_attribute_id`, `parent_template_attribute`) VALUES ('1', '', 'Åtgärder krävs', 'MULTISELECT', '122', '55');

INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value`, `value_type`, `template_attribute_id`, `parent_template_attribute`) VALUES ('1', '', 'Åtgärder krävs', 'MULTISELECT', '123', '64');


