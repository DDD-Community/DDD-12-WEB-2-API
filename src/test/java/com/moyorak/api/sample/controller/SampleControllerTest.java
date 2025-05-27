package com.moyorak.api.sample.controller;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.moyorak.api.config.exception.BusinessException;
import com.moyorak.api.global.domain.ListResponse;
import com.moyorak.api.helper.ConverterHelper;
import com.moyorak.api.helper.TestFixtureUtils;
import com.moyorak.api.sample.domain.Sample;
import com.moyorak.api.sample.domain.SampleFixture;
import com.moyorak.api.sample.dto.SampleResponse;
import com.moyorak.api.sample.dto.SampleSaveRequest;
import com.moyorak.api.sample.dto.SampleSearchRequest;
import com.moyorak.api.sample.dto.SampleUpdateRequest;
import com.moyorak.api.sample.service.SampleService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.util.UriComponentsBuilder;

@WebMvcTest(controllers = SampleController.class)
class SampleControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private SampleService sampleService;

    @Autowired private WebApplicationContext ctx;

    private final String baseUrl = "/sample";

    @BeforeEach
    void init() {
        this.mockMvc =
                MockMvcBuilders.webAppContextSetup(ctx)
                        .addFilters(new CharacterEncodingFilter("UTF-8", true))
                        .alwaysDo(print())
                        .build();
    }

    @Nested
    @DisplayName("샘플 데이터 단 건을 조회할 때,")
    class getDetail {

        @Nested
        @DisplayName("유효성 검증을 하는 과정에서")
        class validate {

            @Nested
            @DisplayName("id 값이")
            class id {

                @ParameterizedTest
                @ValueSource(longs = {-1L, 0L})
                @DisplayName("음수 혹은 0인 경우 유효성 검증에 실패합니다.")
                void isNotPositive(final Long id) throws Exception {
                    // when & then
                    mockMvc.perform(get(baseUrl + "/{id}", id))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest());
                }
            }
        }

        @Test
        @DisplayName("존재하지 않을 경우 400 응답이 내려옵니다.")
        void isNull() throws Exception {
            // given
            final Long id = 1L;

            given(sampleService.getDetail(id)).willThrow(new BusinessException("존재하지 않는 데이터입니다."));

            // when & then
            mockMvc.perform(get(baseUrl + "/{id}", id))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(jsonPath("$.detail").value("존재하지 않는 데이터입니다."))
                    .andReturn();
        }

        @Test
        @DisplayName("성공적으로 조회합니다.")
        void success() throws Exception {
            // given
            final Long id = 1L;
            final String title = "제목";
            final String content = "내용";

            final Sample sample = SampleFixture.fixture(id, title, content);
            final SampleResponse sampleResponse = SampleResponse.from(sample);

            given(sampleService.getDetail(id)).willReturn(sampleResponse);

            // when
            final MvcResult mvcResult =
                    mockMvc.perform(get(baseUrl + "/{id}", id))
                            .andExpect(MockMvcResultMatchers.status().isOk())
                            .andReturn();

            // then
            final SampleResponse response =
                    ConverterHelper.toDto(
                            mvcResult.getResponse().getContentAsString(), SampleResponse.class);
            assertSoftly(
                    it -> {
                        it.assertThat(response.id()).isEqualTo(id);
                        it.assertThat(response.title()).isEqualTo(title);
                        it.assertThat(response.content()).isEqualTo(content);
                    });
        }
    }

    @Nested
    @DisplayName("샘플 데이터를 검색할 때,")
    class search {

        @Nested
        @DisplayName("유효성 검증을 하는 과정에서")
        class validate {

            @Nested
            @DisplayName("제목이")
            class title {

                @Test
                @DisplayName("64자 초과일 때, 유효성 검증에 실패합니다.")
                void isMore() throws Exception {
                    // given
                    final String title = TestFixtureUtils.size에_맞는_문자열_생성(65);

                    // when & then
                    mockMvc.perform(get(getUrl(title, null)))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest());
                }
            }

            @Nested
            @DisplayName("내용이")
            class content {
                @Test
                @DisplayName("200자 초과일 때, 유효성 검증에 실패합니다.")
                void isMore() throws Exception {
                    // given
                    final String content = TestFixtureUtils.size에_맞는_문자열_생성(201);

                    // when & then
                    mockMvc.perform(get(getUrl(null, content)))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest());
                }
            }
        }

        @Test
        @DisplayName("성공적으로 조회합니다.")
        void success() throws Exception {
            // given
            final Long id = 1L;
            final String title = "제목";
            final String content = "내용";

            final Sample sample = SampleFixture.fixture(id, title, content);
            final List<Sample> list = List.of(sample);
            final Page<Sample> page = new PageImpl<>(list, Pageable.ofSize(10), list.size());

            final ListResponse<SampleResponse> sampleResponse =
                    ListResponse.from(page, SampleResponse::from);

            given(sampleService.search(any(SampleSearchRequest.class))).willReturn(sampleResponse);

            // when
            final MvcResult mvcResult =
                    mockMvc.perform(get(getUrl(title, content)))
                            .andExpect(MockMvcResultMatchers.status().isOk())
                            .andReturn();

            // then
            final ListResponse<SampleResponse> response =
                    ConverterHelper.toListResponse(
                            mvcResult.getResponse().getContentAsString(), SampleResponse.class);
            assertSoftly(
                    it -> {
                        it.assertThat(response.getTotalCount()).isEqualTo(1);
                        it.assertThat(response.getData().getFirst().id()).isEqualTo(id);
                        it.assertThat(response.getData().getFirst().title()).isEqualTo(title);
                        it.assertThat(response.getData().getFirst().content()).isEqualTo(content);
                    });
        }

        private String getUrl(final String title, final String content) {
            return UriComponentsBuilder.fromUriString(baseUrl)
                    .queryParam("title", title)
                    .queryParam("content", content)
                    .toUriString();
        }
    }

    @Nested
    @DisplayName("샘플 데이터를 저장할 때,")
    class register {

        @Nested
        @DisplayName("유효성 검증을 하는 과정에서")
        class validate {

            @Nested
            @DisplayName("제목이")
            class title {

                @EmptySource
                @ValueSource(strings = " ")
                @ParameterizedTest
                @DisplayName("빈 값인 경우, 유효성 검증에 실패합니다.")
                void isNull(final String input) throws Exception {
                    // given
                    final String requestBody =
                            """
                        {
                            "title": "%s",
                            "content": "내용"
                        }
                        """
                                    .formatted(input);

                    // when & then
                    mockMvc.perform(
                                    post(baseUrl)
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest());
                }

                @Test
                @DisplayName("누락인 경우, 유효성 검증에 실패합니다.")
                void missing() throws Exception {
                    // given
                    final String requestBody =
                            """
                        {
                            "content": "내용"
                        }
                        """;

                    // when & then
                    mockMvc.perform(
                                    post(baseUrl)
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest());
                }

                @Test
                @DisplayName("64자를 초과했을 경우, 실패합니다.")
                void isMore() throws Exception {
                    final String title = TestFixtureUtils.size에_맞는_문자열_생성(65);

                    final String requestBody =
                            """
                        {
                            "title": "%s",
                            "content": "내용"
                        }
                        """
                                    .formatted(title);

                    // when & then
                    mockMvc.perform(
                                    post(baseUrl)
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest());
                }
            }

            @Nested
            @DisplayName("내용이")
            class content {

                @EmptySource
                @ValueSource(strings = " ")
                @ParameterizedTest
                @DisplayName("빈 값인 경우, 유효성 검증에 실패합니다.")
                void isNull(final String input) throws Exception {
                    // given
                    final String requestBody =
                            """
                        {
                            "title": "제목",
                            "content": "%s"
                        }
                        """
                                    .formatted(input);

                    // when & then
                    mockMvc.perform(
                                    post(baseUrl)
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest());
                }

                @Test
                @DisplayName("누락인 경우, 유효성 검증에 실패합니다.")
                void missing() throws Exception {
                    // given
                    final String requestBody =
                            """
                        {
                            "title": "제목"
                        }
                        """;

                    // when & then
                    mockMvc.perform(
                                    post(baseUrl)
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest());
                }

                @Test
                @DisplayName("200자를 초과했을 경우, 실패합니다.")
                void isMore() throws Exception {
                    // given
                    final String content = TestFixtureUtils.size에_맞는_문자열_생성(201);

                    final String requestBody =
                            """
                        {
                            "title": "제목",
                            "content": "%s"
                        }
                        """
                                    .formatted(content);

                    // when & then
                    mockMvc.perform(
                                    post(baseUrl)
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest());
                }
            }
        }

        @Test
        @DisplayName("성공적으로 저장합니다.")
        void success() throws Exception {
            // given
            final String requestBody =
                    """
                {
                    "title": "제목",
                    "content": "내용"
                }
                """;

            willDoNothing().given(sampleService).register(any(SampleSaveRequest.class));

            // when & then
            mockMvc.perform(
                            post(baseUrl)
                                    .content(requestBody)
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }

    @Nested
    @DisplayName("샘플 데이터를 수정할 때,")
    class modify {

        @Nested
        @DisplayName("유효성 검증을 하는 과정에서")
        class validate {

            @Nested
            @DisplayName("제목이")
            class title {

                @EmptySource
                @ValueSource(strings = " ")
                @ParameterizedTest
                @DisplayName("빈 값인 경우, 유효성 검증에 실패합니다.")
                void isNull(final String input) throws Exception {
                    // given
                    final Long id = 1L;

                    final String requestBody =
                            """
                        {
                            "title": "%s",
                            "content": "내용"
                        }
                        """
                                    .formatted(input);

                    // when & then
                    mockMvc.perform(
                                    put(baseUrl + "/{id}", id)
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest());
                }

                @Test
                @DisplayName("누락인 경우, 유효성 검증에 실패합니다.")
                void missing() throws Exception {
                    // given
                    final Long id = 1L;

                    final String requestBody =
                            """
                        {
                            "content": "내용"
                        }
                        """;

                    // when & then
                    mockMvc.perform(
                                    put(baseUrl + "/{id}", id)
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest());
                }

                @Test
                @DisplayName("64자를 초과했을 경우, 실패합니다.")
                void isMore() throws Exception {
                    // given
                    final Long id = 1L;

                    final String title = TestFixtureUtils.size에_맞는_문자열_생성(65);

                    final String requestBody =
                            """
                        {
                            "title": "%s",
                            "content": "내용"
                        }
                        """
                                    .formatted(title);

                    // when & then
                    mockMvc.perform(
                                    put(baseUrl + "/{id}", id)
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest());
                }
            }

            @Nested
            @DisplayName("내용이")
            class content {

                @EmptySource
                @ValueSource(strings = " ")
                @ParameterizedTest
                @DisplayName("빈 값인 경우, 유효성 검증에 실패합니다.")
                void isNull(final String input) throws Exception {
                    // given
                    final Long id = 1L;

                    final String requestBody =
                            """
                        {
                            "title": "제목",
                            "content": "%s"
                        }
                        """
                                    .formatted(input);

                    // when & then
                    mockMvc.perform(
                                    put(baseUrl + "/{id}", id)
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest());
                }

                @Test
                @DisplayName("누락인 경우, 유효성 검증에 실패합니다.")
                void missing() throws Exception {
                    // given
                    final Long id = 1L;

                    final String requestBody =
                            """
                        {
                            "title": "제목"
                        }
                        """;

                    // when & then
                    mockMvc.perform(
                                    put(baseUrl + "/{id}", id)
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest());
                }

                @Test
                @DisplayName("200자를 초과했을 경우, 실패합니다.")
                void isMore() throws Exception {
                    // given
                    final Long id = 1L;

                    final String content = TestFixtureUtils.size에_맞는_문자열_생성(201);

                    final String requestBody =
                            """
                        {
                            "title": "제목",
                            "content": "%s"
                        }
                        """
                                    .formatted(content);

                    // when & then
                    mockMvc.perform(
                                    put(baseUrl + "/{id}", id)
                                            .content(requestBody)
                                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest());
                }
            }
        }

        @Test
        @DisplayName("존재하지 않을 경우 400 응답이 내려옵니다.")
        void isNull() throws Exception {
            // given
            final Long id = 1L;
            final String requestBody =
                    """
                        {
                            "title": "제목",
                            "content": "%s"
                        }
                        """;

            willThrow(new BusinessException("존재하지 않는 데이터입니다."))
                    .given(sampleService)
                    .modify(eq(id), any(SampleUpdateRequest.class));

            // when & then
            mockMvc.perform(
                            put(baseUrl + "/{id}", id)
                                    .content(requestBody)
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(jsonPath("$.detail").value("존재하지 않는 데이터입니다."))
                    .andReturn();
        }

        @Test
        @DisplayName("성공적으로 수정합니다.")
        void success() throws Exception {
            // given
            final Long id = 1L;

            final String requestBody =
                    """
                {
                    "title": "제목",
                    "content": "내용"
                }
                """;

            willDoNothing().given(sampleService).modify(eq(id), any(SampleUpdateRequest.class));

            // when & then
            mockMvc.perform(
                            put(baseUrl + "/{id}", id)
                                    .content(requestBody)
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }

    @Nested
    @DisplayName("샘플 데이터를 삭제할 때,")
    class remove {

        @Nested
        @DisplayName("유효성 검증을 하는 과정에서")
        class validate {

            @Nested
            @DisplayName("id 값이")
            class id {

                @ParameterizedTest
                @ValueSource(longs = {-1L, 0L})
                @DisplayName("음수 혹은 0인 경우 유효성 검증에 실패합니다.")
                void isNotPositive(final Long id) throws Exception {
                    // when & then
                    mockMvc.perform(delete(baseUrl + "/{id}", id))
                            .andExpect(MockMvcResultMatchers.status().isBadRequest());
                }
            }

            @Test
            @DisplayName("존재하지 않을 경우 400 응답이 내려옵니다.")
            void isNull() throws Exception {
                // given
                final Long id = 1L;

                willThrow(new BusinessException("존재하지 않는 데이터입니다.")).given(sampleService).remove(id);

                // when & then
                mockMvc.perform(delete(baseUrl + "/{id}", id))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest())
                        .andExpect(jsonPath("$.detail").value("존재하지 않는 데이터입니다."))
                        .andReturn();
            }

            @Test
            @DisplayName("성공적으로 삭제합니다.")
            void success() throws Exception {
                // given
                final Long id = 1L;

                willDoNothing().given(sampleService).remove(id);

                // when & then

                mockMvc.perform(delete(baseUrl + "/{id}", id))
                        .andExpect(MockMvcResultMatchers.status().isOk());
            }
        }
    }
}
