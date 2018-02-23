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

INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value_type`, `template_attribute_id`, `description`, `required`, `sort_prio`) VALUES ('1', 'Utlämnande av uppgifter', 'BOOL', '66', 'Lämnas ut personuppgifter till någon/några utanför organisationen?', '0', '155');
INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value_type`, `template_attribute_id`, `description`, `required`, `regex`, `sort_prio`, `parent_template_attribute`, `show_only_when_value_equals`) VALUES ('1', 'Till vilka lämnas personuppgifterna ut utanför organisationen?', 'STRINGBOX', '67', 'ex support, skatteverket, försäkringskassan, pensionsförvaltare, tjänsteleverantörer osv', '0', NULL, '999', '66', 'true');
INSERT INTO `register.sundsvall.se`.`node_template_attributes` (`parent_type_id`, `name`, `value`, `value_type`, `template_attribute_id`, `required`, `sort_prio`, `parent_template_attribute`) VALUES ('1', '', 'Åtgärder krävs', 'MULTISELECT', '124', '0', '999', '66');

UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='221' WHERE `template_attribute_id`='12';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='222' WHERE `template_attribute_id`='13';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='223' WHERE `template_attribute_id`='14';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='131' WHERE `template_attribute_id`='18';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='181' WHERE `template_attribute_id`='22';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='141' WHERE `template_attribute_id`='26';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='201' WHERE `template_attribute_id`='28';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='202' WHERE `template_attribute_id`='29';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='211' WHERE `template_attribute_id`='31';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='213' WHERE `template_attribute_id`='32';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='212' WHERE `template_attribute_id`='33';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='111' WHERE `template_attribute_id`='36';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='231' WHERE `template_attribute_id`='38';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='121' WHERE `template_attribute_id`='39';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='191' WHERE `template_attribute_id`='41';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='161' WHERE `template_attribute_id`='43';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='51' WHERE `template_attribute_id`='44';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='52' WHERE `template_attribute_id`='45';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='31' WHERE `template_attribute_id`='47';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='61' WHERE `template_attribute_id`='48';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='224' WHERE `template_attribute_id`='49';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='225' WHERE `template_attribute_id`='50';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='226' WHERE `template_attribute_id`='51';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='227' WHERE `template_attribute_id`='52';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='151' WHERE `template_attribute_id`='53';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='241' WHERE `template_attribute_id`='56';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='231' WHERE `template_attribute_id`='57';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='232' WHERE `template_attribute_id`='58';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='233' WHERE `template_attribute_id`='59';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='234' WHERE `template_attribute_id`='60';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='235' WHERE `template_attribute_id`='61';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='236' WHERE `template_attribute_id`='62';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='237' WHERE `template_attribute_id`='63';
UPDATE `register.sundsvall.se`.`node_template_attributes` SET `sort_prio`='156' WHERE `template_attribute_id`='67';

UPDATE `register.sundsvall.se`.`node_template_attributes` SET `value`='Öppet, ingen inloggning|Inloggning som anställd|Inloggning som medborgare|Inloggning med särskild roll|Systemadmin med personligt konto' WHERE `template_attribute_id`='9';




