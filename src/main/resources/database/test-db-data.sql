
-------------------------------------- USERS ---------------------------------------

INSERT INTO tdlschema.users SET
    id = UUID'00000000-0000-0000-0000-000000000001',
    username = 'user',
    password = 'Ab12345';

INSERT INTO tdlschema.users SET
    id = UUID'00000000-0000-0000-0000-000000000002',
    username = 'user_A',
    password = 'Ab12345';

INSERT INTO tdlschema.users SET
    id = UUID'00000000-0000-0000-0000-000000000003',
    username = 'user_B',
    password = 'Ab12345';

------------------------------------ LIST ITEMS ------------------------------------

INSERT INTO tdlschema.list_items SET
    id = UUID'00000000-0000-0000-0001-000000000001',
    owner_id = UUID'00000000-0000-0000-0000-000000000001',
    message = 'TASK A',
    is_done = true,
    priority = 1,
    priority_change_number = 1;

INSERT INTO tdlschema.list_items SET
    id = UUID'00000000-0000-0000-0001-000000000002',
    owner_id = UUID'00000000-0000-0000-0000-000000000001',
    message = 'TASK B',
    is_done = false,
    priority = 2,
    priority_change_number = 2;

INSERT INTO tdlschema.list_items SET
    id = UUID'00000000-0000-0000-0001-000000000003',
    owner_id = UUID'00000000-0000-0000-0000-000000000001',
    message = 'TASK C',
    is_done = true,
    priority = 3,
    priority_change_number = 3;

INSERT INTO tdlschema.list_items SET
    id = UUID'00000000-0000-0000-0001-000000000004',
    owner_id = UUID'00000000-0000-0000-0000-000000000001',
    message = 'TASK D',
    is_done = false,
    priority = 4,
    priority_change_number = 4;

INSERT INTO tdlschema.list_items SET
    id = UUID'00000000-0000-0000-0001-000000000005',
    owner_id = UUID'00000000-0000-0000-0000-000000000001',
    message = 'TASK E',
    is_done = false,
    priority = 5,
    priority_change_number = 5;
