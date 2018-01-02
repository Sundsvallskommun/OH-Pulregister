
--fill the group_ids column with the initial values
UPDATE node_template_attributes SET node_template_attributes.group_ids =
(SELECT GROUP_CONCAT(simple_groups.groupID SEPARATOR '|')
FROM
simple_groups
WHERE groupID>=14)
WHERE node_template_attributes.template_attribute_id=2; 


-- Add group to Personuppgiftsansvarig value when new group is created
CREATE DEFINER=`root`@`localhost` TRIGGER `register.sundsvall.se`.`simple_groups_AFTER_INSERT` AFTER INSERT ON `simple_groups` FOR EACH ROW
BEGIN

-- Add new groupID to group_ids column
SET @groupIDs = 
(SELECT node_template_attributes.group_ids
FROM  node_template_attributes
WHERE node_template_attributes.template_attribute_id=2
);
UPDATE node_template_attributes SET node_template_attributes.group_ids = CONCAT(@groupIDs, '|', NEW.groupID)
WHERE node_template_attributes.template_attribute_id=2;

-- Add new name to value column
SET @groupNames = 
(SELECT node_template_attributes.value
FROM  node_template_attributes
WHERE node_template_attributes.template_attribute_id=2
);
UPDATE node_template_attributes SET node_template_attributes.value = CONCAT(@groupNames, '|', NEW.name)
WHERE node_template_attributes.template_attribute_id=2;

END




-- Remove the group from Personuppgiftsansvarig when group is removed
CREATE DEFINER=`root`@`localhost` TRIGGER `register.sundsvall.se`.`simple_groups_AFTER_DELETE` AFTER DELETE ON `simple_groups` FOR EACH ROW
BEGIN

SET @counter = 0;
SET @foundIndex = 'false';
SET @groupIDs = 
(SELECT node_template_attributes.group_ids
FROM  node_template_attributes
WHERE node_template_attributes.template_attribute_id=2
);
SET @groupNames = 
(SELECT node_template_attributes.value
FROM  node_template_attributes
WHERE node_template_attributes.template_attribute_id=2
);

-- Find the groupindex
WHILE @foundIndex = 'false' DO
    SET @counter = @counter + 1;
    SET @selectedID =  SUBSTRING_INDEX(@groupIDs, "|", 1); 
    SET @groupIDs = SUBSTRING(@groupIDs, CHAR_LENGTH(@selectedID)+1+1) ;
    
    IF @selectedID = OLD.groupID
        THEN SET @foundIndex = 'true';
    END IF;
    IF @counter > CHAR_LENGTH(@groupNames)
        THEN SET @foundIndex = 'notFound';
    END IF;
END WHILE;

-- remove the group name with the found index
IF @foundIndex = 'true' THEN 
    IF @counter = 1 THEN
        SET @deletedName = SUBSTRING_INDEX(@groupNames, "|", 1);
        UPDATE node_template_attributes SET node_template_attributes.value = 
            SUBSTRING(@groupNames, CHAR_LENGTH(@deletedName)+1)
        WHERE node_template_attributes.template_attribute_id=2;
    ELSE
        SET @beforeDeletedName = SUBSTRING_INDEX(@groupNames, "|", @counter-1);
        SET @groupNames = SUBSTRING(@groupNames, CHAR_LENGTH(@beforeDeletedName)+1+1);
        SET @deletedName = SUBSTRING_INDEX(@groupNames, "|", 1);
        SET @afterDeletedName = SUBSTRING(@groupNames, CHAR_LENGTH(@deletedName)+1);
        UPDATE node_template_attributes SET node_template_attributes.value = 
            CONCAT(@beforeDeletedName,'|',@afterDeletedName)
        WHERE node_template_attributes.template_attribute_id=2;
    END IF;
END IF;

END



-- Update the group from Personuppgiftsansvarig when group is updated
CREATE DEFINER=`root`@`localhost` TRIGGER `register.sundsvall.se`.`simple_groups_AFTER_UPDATE` AFTER UPDATE ON `simple_groups` FOR EACH ROW
BEGIN

SET @counter = 0;
SET @foundIndex = 'false';
SET @groupIDs = 
(SELECT node_template_attributes.group_ids
FROM  node_template_attributes
WHERE node_template_attributes.template_attribute_id=2
);
SET @groupNames = 
(SELECT node_template_attributes.value
FROM  node_template_attributes
WHERE node_template_attributes.template_attribute_id=2
);

-- Find the groupindex
WHILE @foundIndex = 'false' DO
    SET @counter = @counter + 1;
    SET @selectedID =  SUBSTRING_INDEX(@groupIDs, "|", 1); 
    SET @groupIDs = SUBSTRING(@groupIDs, CHAR_LENGTH(@selectedID)+1+1) ;
    
    IF @selectedID = OLD.groupID
        THEN SET @foundIndex = 'true';
    END IF;
    IF @counter > CHAR_LENGTH(@groupNames)
        THEN SET @foundIndex = 'notFound';
    END IF;
END WHILE;

-- Update the group name with the found index by deleting and reinserting
IF @foundIndex = 'true' THEN 
    IF @counter = 1 THEN
        SET @deletedName = SUBSTRING_INDEX(@groupNames, "|", 1);
        UPDATE node_template_attributes SET node_template_attributes.value = 
            CONCAT(new.name,SUBSTRING(@groupNames, CHAR_LENGTH(@deletedName)+1))
        WHERE node_template_attributes.template_attribute_id=2;
    ELSE
        SET @beforeDeletedName = SUBSTRING_INDEX(@groupNames, "|", @counter-1);
        SET @groupNames = SUBSTRING(@groupNames, CHAR_LENGTH(@beforeDeletedName)+1+1);
        SET @deletedName = SUBSTRING_INDEX(@groupNames, "|", 1);
        SET @afterDeletedName = SUBSTRING(@groupNames, CHAR_LENGTH(@deletedName)+1);
        UPDATE node_template_attributes SET node_template_attributes.value = 
            CONCAT(@beforeDeletedName,'|',new.name,@afterDeletedName)
        WHERE node_template_attributes.template_attribute_id=2;
    END IF;
END IF;

END
