package S10.VVSS.lab1.test.rest;

import S10.VVSS.lab1.AuthenticatedAbstractTest;
import S10.VVSS.lab1.entities.listitem.ListItem;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("list-item")
@Transactional(readOnly = true)
public class ListItemControllerTest extends AuthenticatedAbstractTest {

    @Autowired
    public ListItemControllerTest(WebApplicationContext context) {
        super(context);
    }

    @Test
    @DisplayName("get all items success")
    @Tag("get") @Tag("success")
    public void getAll_success() throws Exception{
        List<ListItem> list = listItemService.findAllByOwner(user);

        mockMvc.perform(get("/api/list-item/find-by/user"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$").isArray(),
                        jsonPath("$[*].id", contains( list.stream().map(i -> i.getId().toString())
                                .toArray(String[]::new) )),
                        jsonPath("$[*].message", contains( list.stream().map(ListItem::getMessage)
                                .toArray(String[]::new) )),
                        jsonPath("$[*].isDone", contains( list.stream().map(ListItem::getDone)
                                .toArray(Boolean[]::new) )) );
    }

    @Test
    @DisplayName("get item by id success")
    @Tag("get") @Tag("success")
    public void getById_success() throws Exception{
        UUID id = UUID.fromString("00000000-0000-0000-0001-000000000001");
        ListItem item = listItemService.findById(id).orElse(null);

        Assumptions.assumeFalse(item == null, "item not found");
        Assumptions.assumeTrue(item.getOwner().equals(user), "wrong owner of item");

        mockMvc.perform(get("/api/list-item/{id}", id))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id", is(id.toString()) ),
                        jsonPath("$.message", is(item.getMessage()) ),
                        jsonPath("$.isDone", is(item.getDone()) ));
    }

    @Test
    @DisplayName("get item by id fail - id does not exist")
    @Tag("get") @Tag("fail")
    public void getById_fail_idNotExists() throws Exception{
        UUID id = UUID.randomUUID();

        Assumptions.assumeFalse(listItemService.findById(id).isPresent(), "item is found");

        mockMvc.perform(get("/api/list-item/{id}", id))
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.id").doesNotExist(),
                        jsonPath("$.isDone").doesNotExist(),
                        jsonPath("$.code", is(1100)),
                        jsonPath("$.message").isString() );
    }

    @Test
    @DisplayName("get item by id fail - wrong owner")
    @Tag("get") @Tag("fail")
    public void getById_fail_wrongOwner() throws Exception{
        UUID id = UUID.fromString("00000000-0000-0000-0001-000000000011");
        ListItem item = listItemService.findById(id).orElse(null);

        Assumptions.assumeFalse(item == null, "item not found");
        Assumptions.assumeFalse(item.getOwner().equals(user), "correct owner of item");

        mockMvc.perform(get("/api/list-item/{id}", id))
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.id").doesNotExist(),
                        jsonPath("$.isDone").doesNotExist(),
                        jsonPath("$.code", is(1100)),
                        jsonPath("$.message").isString() );
    }

    @Test
    @DisplayName("create item success")
    @Tag("create") @Tag("success")
    public void create_success() throws Exception {
        final String message = "msg";
        final Boolean isDone = false;

        ObjectNode jsonRequestBody = mapper.createObjectNode();
        jsonRequestBody.put("message", message);
        jsonRequestBody.put("isDone", isDone);

        mockMvc.perform(post("/api/list-item")
                        .content(jsonRequestBody.toString()))
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.id", notNullValue()),
                        jsonPath("$.message", is(message)),
                        jsonPath("$.isDone", is(isDone))
                );
    }

    public Stream<Arguments> invalidMessages() {
        return Stream.of(
                Arguments.of(Named.of("is null", null), 1200),
                Arguments.of(Named.of("is blank", " \t"), 1206),
                Arguments.of(Named.of("too long", buildRepeatableString("A", 101)), 1205)
        );
    }

    @ParameterizedTest(name = parametrizedTestPattern)
    @DisplayName("create item fail - invalid message")
    @Tag("create") @Tag("fail")
    @MethodSource("invalidMessages")
    public void create_fail_invalidMessage(String message, int code) throws Exception {

        ObjectNode jsonRequestBody = mapper.createObjectNode();
        jsonRequestBody.put("message", message);
        jsonRequestBody.put("isDone", true);

        mockMvc.perform(post("/api/list-item")
                        .content(jsonRequestBody.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.id").doesNotExist(),
                        jsonPath("$.isDone").doesNotExist(),
                        jsonPath("$.code", is(code)),
                        jsonPath("$.message").isString() );
    }

    public Stream<Arguments> invalidStatuses() {
        return Stream.of(
                Arguments.of(Named.of("is null", null), 1200)
        );
    }

    @ParameterizedTest(name = parametrizedTestPattern)
    @DisplayName("create item fail - invalid status")
    @Tag("create") @Tag("fail")
    @MethodSource("invalidStatuses")
    public void create_fail_invalidMessage(Boolean status, int code) throws Exception {
        ObjectNode jsonRequestBody = mapper.createObjectNode();
        jsonRequestBody.put("message", "msg");
        jsonRequestBody.put("isDone", status);

        mockMvc.perform(post("/api/list-item")
                        .content(jsonRequestBody.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.id").doesNotExist(),
                        jsonPath("$.isDone").doesNotExist(),
                        jsonPath("$.code", is(code)),
                        jsonPath("$.message").isString() );
    }

    @Test
    @DisplayName("update item success")
    @Tag("update") @Tag("success")
    public void update_success() throws Exception{
        final String message = "msg";
        final Boolean isDone = false;
        final UUID id = UUID.fromString("00000000-0000-0000-0001-000000000001");
        ListItem item = listItemService.findById(id).orElse(null);

        Assumptions.assumeFalse(item == null, "item not found");
        Assumptions.assumeTrue(item.getOwner().equals(user), "wrong owner of item");

        ObjectNode jsonRequestBody = mapper.createObjectNode();
        jsonRequestBody.put("message", message);
        jsonRequestBody.put("isDone", isDone);

        mockMvc.perform(put("/api/list-item/{id}", id)
                        .content(jsonRequestBody.toString()))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id", is(id.toString()) ),
                        jsonPath("$.message", is(message) ),
                        jsonPath("$.isDone", is(isDone) ));
    }

    @ParameterizedTest(name = parametrizedTestPattern)
    @DisplayName("update item fail - invalid message")
    @Tag("update") @Tag("fail")
    @MethodSource("invalidMessages")
    public void update_fail_invalidMessage(String message, int code) throws Exception {
        final UUID id = UUID.fromString("00000000-0000-0000-0001-000000000001");
        ListItem item = listItemService.findById(id).orElse(null);

        Assumptions.assumeFalse(item == null, "item not found");
        Assumptions.assumeTrue(item.getOwner().equals(user), "wrong owner of item");

        ObjectNode jsonRequestBody = mapper.createObjectNode();
        jsonRequestBody.put("message", message);
        jsonRequestBody.put("isDone", false);

        mockMvc.perform(put("/api/list-item/{id}", id)
                        .content(jsonRequestBody.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.id").doesNotExist(),
                        jsonPath("$.isDone").doesNotExist(),
                        jsonPath("$.code", is(code)),
                        jsonPath("$.message").isString() );
    }

    @ParameterizedTest(name = parametrizedTestPattern)
    @DisplayName("update item fail - invalid status")
    @Tag("update") @Tag("fail")
    @MethodSource("invalidStatuses")
    public void update_fail_invalidStatus(Boolean status, int code) throws Exception {
        final UUID id = UUID.fromString("00000000-0000-0000-0001-000000000001");
        ListItem item = listItemService.findById(id).orElse(null);

        Assumptions.assumeFalse(item == null, "item not found");
        Assumptions.assumeTrue(item.getOwner().equals(user), "wrong owner of item");

        ObjectNode jsonRequestBody = mapper.createObjectNode();
        jsonRequestBody.put("message", "msg");
        jsonRequestBody.put("isDone", status);

        mockMvc.perform(put("/api/list-item/{id}", id)
                        .content(jsonRequestBody.toString()))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.id").doesNotExist(),
                        jsonPath("$.isDone").doesNotExist(),
                        jsonPath("$.code", is(code)),
                        jsonPath("$.message").isString() );
    }

    @Test
    @DisplayName("update item fail - id does not exist")
    @Tag("update") @Tag("fail")
    public void update_fail_idNotExists() throws Exception {
        final UUID id = UUID.randomUUID();

        Assumptions.assumeFalse(listItemService.findById(id).isPresent(), "item is found");

        ObjectNode jsonRequestBody = mapper.createObjectNode();
        jsonRequestBody.put("message", "msg");
        jsonRequestBody.put("isDone", false);

        mockMvc.perform(put("/api/list-item/{id}", id)
                        .content(jsonRequestBody.toString()))
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.id").doesNotExist(),
                        jsonPath("$.isDone").doesNotExist(),
                        jsonPath("$.code", is(1100)),
                        jsonPath("$.message").isString() );
    }

    @Test
    @DisplayName("update item fail - wrong owner")
    @Tag("update") @Tag("fail")
    public void update_fail_wrongOwner() throws Exception {
        final UUID id = UUID.fromString("00000000-0000-0000-0001-000000000011");
        ListItem item = listItemService.findById(id).orElse(null);

        Assumptions.assumeFalse(item == null, "item not found");
        Assumptions.assumeFalse(item.getOwner().equals(user), "correct owner of item");

        ObjectNode jsonRequestBody = mapper.createObjectNode();
        jsonRequestBody.put("message", "msg");
        jsonRequestBody.put("isDone", false);

        mockMvc.perform(put("/api/list-item/{id}", id)
                        .content(jsonRequestBody.toString()))
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.id").doesNotExist(),
                        jsonPath("$.isDone").doesNotExist(),
                        jsonPath("$.code", is(1100)),
                        jsonPath("$.message").isString() );
    }

    @Test
    @DisplayName("update item order success")
    @Tag("update") @Tag("success")
    @Transactional(readOnly = false)
    public void updateOrder_success() throws Exception{
        final int newIndex = 2;
        final UUID id = UUID.fromString("00000000-0000-0000-0001-000000000005");
        ListItem item = listItemService.findById(id).orElse(null);

        Assumptions.assumeFalse(item == null, "item not found");
        Assumptions.assumeTrue(item.getOwner().equals(user), "wrong owner of item");

        ObjectNode jsonRequestBody = mapper.createObjectNode();
        jsonRequestBody.put("order", newIndex);

        restartTransaction();

        mockMvc.perform(put("/api/list-item/{id}/order", id)
                        .content(jsonRequestBody.toString()))
                .andExpectAll(
                        status().isOk(),
                        jsonPath(String.format("$[%d].id", newIndex - 1), is(id.toString()) ));
    }

    @Test
    @DisplayName("update item order fail - id does not exist")
    @Tag("update") @Tag("fail")
    public void updateOrder_fail_idNotExists() throws Exception {
        final UUID id = UUID.randomUUID();

        Assumptions.assumeFalse(listItemService.findById(id).isPresent(), "item is found");

        ObjectNode jsonRequestBody = mapper.createObjectNode();
        jsonRequestBody.put("order", 2);

        mockMvc.perform(put("/api/list-item/{id}/order", id)
                        .content(jsonRequestBody.toString()))
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.code", is(1100)),
                        jsonPath("$.message").isString() );
    }

    @Test
    @DisplayName("update item order fail - wrong owner")
    @Tag("update") @Tag("fail")
    public void updateOrder_fail_wrongOwner() throws Exception {
        final UUID id = UUID.fromString("00000000-0000-0000-0001-000000000011");
        ListItem item = listItemService.findById(id).orElse(null);

        Assumptions.assumeFalse(item == null, "item not found");
        Assumptions.assumeFalse(item.getOwner().equals(user), "correct owner of item");

        ObjectNode jsonRequestBody = mapper.createObjectNode();
        jsonRequestBody.put("order", 2);

        mockMvc.perform(put("/api/list-item/{id}/order", id)
                        .content(jsonRequestBody.toString()))
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.code", is(1100)),
                        jsonPath("$.message").isString() );
    }

    @Test
    @DisplayName("delete item success")
    @Tag("delete") @Tag("success")
    @Transactional(readOnly = false)
    public void delete_success() throws Exception{
        final UUID id = UUID.fromString("00000000-0000-0000-0001-000000000001");
        ListItem item = listItemService.findById(id).orElse(null);

        Assumptions.assumeFalse(item == null, "item not found");
        Assumptions.assumeTrue(item.getOwner().equals(user), "wrong owner of item");

        restartTransaction();

        mockMvc.perform(delete("/api/list-item/{id}", id))
                .andExpectAll(
                        status().isNoContent());

        Assertions.assertFalse(listItemService.findById(id).isPresent());
    }

    @Test
    @DisplayName("delete item order fail - id does not exist")
    @Tag("delete") @Tag("fail")
    public void delete_fail_idNotExists() throws Exception {
        final UUID id = UUID.randomUUID();

        Assumptions.assumeFalse(listItemService.findById(id).isPresent(), "item is found");

        mockMvc.perform(delete("/api/list-item/{id}", id))
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.code", is(1100)),
                        jsonPath("$.message").isString() );
    }

    @Test
    @DisplayName("delete item order fail - wrong owner")
    @Tag("delete") @Tag("fail")
    public void delete_fail_wrongOwner() throws Exception {
        final UUID id = UUID.fromString("00000000-0000-0000-0001-000000000011");
        ListItem item = listItemService.findById(id).orElse(null);

        Assumptions.assumeFalse(item == null, "item not found");
        Assumptions.assumeFalse(item.getOwner().equals(user), "correct owner of item");

        mockMvc.perform(delete("/api/list-item/{id}", id))
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.code", is(1100)),
                        jsonPath("$.message").isString() );
    }

}
