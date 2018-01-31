ALTER TABLE `register.sundsvall.se`.`node_template_attributes` 
CHANGE COLUMN `sort_prio` `sort_prio` INT(10) UNSIGNED NULL DEFAULT '999' ;

UPDATE `register.sundsvall.se`.node_template_attributes
SET sort_prio = '999'
WHERE template_attribute_id != 1;

UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='10' WHERE `template_attribute_id`='1';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='20' WHERE `template_attribute_id`='2';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='30' WHERE `template_attribute_id`='46';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='50' WHERE `template_attribute_id`='4';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='60' WHERE `template_attribute_id`='5';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='40' WHERE `template_attribute_id`='6';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='70' WHERE `template_attribute_id`='7';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='80' WHERE `template_attribute_id`='8';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='90' WHERE `template_attribute_id`='9';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='100' WHERE `template_attribute_id`='10';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='220' WHERE `template_attribute_id`='11';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='110' WHERE `template_attribute_id`='15';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='120' WHERE `template_attribute_id`='16';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='130' WHERE `template_attribute_id`='17';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='150' WHERE `template_attribute_id`='19';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='170' WHERE `template_attribute_id`='20';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='180' WHERE `template_attribute_id`='21';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='200' WHERE `template_attribute_id`='23';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='140' WHERE `template_attribute_id`='25';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='210' WHERE `template_attribute_id`='30';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='230' WHERE `template_attribute_id`='37';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='190' WHERE `template_attribute_id`='40';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='160' WHERE `template_attribute_id`='42';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='240' WHERE `template_attribute_id`='55';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='250' WHERE `template_attribute_id`='64';

UPDATE `register.sundsvall.se`.`node_template_attributes` SET `value`='Samtliga användare har samma nivå av behörighet i systemet|Behörighet tilldelas till användare utefter arbetsuppgift|Vet ej' WHERE `template_attribute_id`='10';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `value`='För evigt|Gallras, ange antal år de sparas|Vej ej' WHERE `template_attribute_id`='21';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `value_type`='STRINGBOX' WHERE `template_attribute_id`='22';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `show_only_when_value_equals`='never' WHERE `template_attribute_id`='13';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `show_only_when_value_equals`='1|2' WHERE `template_attribute_id`='12';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `show_only_when_value_equals`='never' WHERE `template_attribute_id`='50';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `show_only_when_value_equals`='never' WHERE `template_attribute_id`='51';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `show_only_when_value_equals`='never' WHERE `template_attribute_id`='52';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `show_only_when_value_equals`='1|2' WHERE `template_attribute_id`='49';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `show_only_when_value_equals`='1|2' WHERE `template_attribute_id`='14';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `description`='Exempelvis Företag1, Företag 2' WHERE `template_attribute_id`='49';


UPDATE `register.sundsvall.se`.`node_template_attributes` SET `description`='Vilka personuppgifter kan finnas om man lyckas med utgallring? Att kunna gallra ut en personuppgift innebär att man genom t ex uteslutningsmetoden eller via en specifik kunskap kan identifiera en viss person via en informationsmängd som i första anblick inte innehåller några direkta personuppgifter.' WHERE `template_attribute_id`='44';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `description`='Ange ändamålet med personuppgiftsbehandlingen. Om man samlar in personuppgifter ska ändamålen vara bestämda innan insamlingen. Ändamålen ska informeras till de registrerade, de får inte vara för allmänt hållna utan måste vara specificerade i viss mån. Man får inte byta ändamål med personuppgiftsbehandlingen efter insamlande hur som helst.' WHERE `template_attribute_id`='6';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `description`='Kräver tillgången till personuppgifter någon form av inloggning med ett användarkonto? Ange den typ av användarkonto/inloggning som krävs för att komma åt personuppgifter.' WHERE `template_attribute_id`='9';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `description`='Hur ser tilldelningen av behörigheter ut? Behörigheter ska vara begränsade till vad som krävs för att utföra de arbetsuppgifter användaren har. Samma användarkonto ska inte användas för t ex drift, systemadministration och vanlig användning.' WHERE `template_attribute_id`='10';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `description`='Föreligger biträdesrelation? Ett personuppgiftsbiträde definieras som en fysisk eller juridisk person, offentlig myndighet eller annat organ som behandlar personuppgifter för den personuppgiftsansvariges räkning. Om den personuppgiftsansvarige lagrar sina uppgifter hos en extern molntjänstleverantör är leverantören att betrakta som biträde. Exempelvis om ni köper drift av ett IT-stöd från en extern part (molntjänst/outsourcing) eller att ni lagrar personuppgifter hos en extern part.' WHERE `template_attribute_id`='11';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `description`='Behövs samtycke? Som utgångspunkt krävs samtycke om man inte kan utföra personuppgiftsbehandling på någon annan rättslig grund som anges i 9§ PuL eller i dataskyddsförordningens motsvarighet artikel 9. Det behövs till exempel inte samtycke vid insamlande av uppgifter som krävs för myndighetsutövning. ' WHERE `template_attribute_id`='16';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `value`='För evigt|Gallras|Vej ej' WHERE `template_attribute_id`='21';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `name`='Beskriv rutiner för gallring, t ex hur länge personuppgifter sparas' WHERE `template_attribute_id`='22';

