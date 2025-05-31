package com.moyorak.api.sample.service;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.moyorak.api.sample.domain.Sample;
import com.moyorak.api.sample.domain.SampleFixture;
import com.moyorak.api.sample.dto.SampleResponse;
import com.moyorak.api.sample.dto.SampleSaveRequest;
import com.moyorak.api.sample.dto.SampleSaveRequestFixture;
import com.moyorak.api.sample.dto.SampleSearchRequest;
import com.moyorak.api.sample.dto.SampleSearchRequestFixture;
import com.moyorak.api.sample.dto.SampleUpdateRequest;
import com.moyorak.api.sample.dto.SampleUpdateRequestFixture;
import com.moyorak.api.sample.repository.SampleRepository;
import com.moyorak.config.exception.BusinessException;
import com.moyorak.global.domain.ListResponse;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;

@ExtendWith(MockitoExtension.class)
class SampleServiceTest {

    @InjectMocks private SampleService sampleService;

    @Mock private SampleRepository sampleRepository;

    @Nested
    @DisplayName("샘플 데이터 단 건을 조회할 때,")
    class getDetail {

        @Test
        @DisplayName("데이터가 존재하지 않을 경우 오류가 발생합니다.")
        void isNull() {
            // given
            final Long id = 1L;

            given(sampleRepository.findById(id)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> sampleService.getDetail(id))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("존재하지 않는 데이터입니다.");
        }

        @Test
        @DisplayName("성공적으로 조회합니다.")
        void success() {
            // given
            final Long id = 1L;
            final Sample sample = SampleFixture.fixture(id, "제목", "내용");

            given(sampleRepository.findById(id)).willReturn(Optional.of(sample));

            // when
            final SampleResponse result = sampleService.getDetail(id);

            // then
            assertSoftly(
                    it -> {
                        it.assertThat(result.id()).isEqualTo(id);
                        it.assertThat(result.title()).isEqualTo(sample.getTitle());
                        it.assertThat(result.content()).isEqualTo(sample.getContent());
                    });
        }

        @Nested
        @DisplayName("샘플 데이터를 검색할 때,")
        class search {

            @Test
            @DisplayName("성공적으로 데이터를 조회합니다.")
            void success() {
                // given
                final Long id = 1L;
                final String title = "제목";
                final String content = "내용";

                final SampleSearchRequest request =
                        SampleSearchRequestFixture.fixture(title, content);
                final Sample sample = SampleFixture.fixture(id, title, content);
                final List<Sample> list = List.of(sample);

                given(
                                sampleRepository.findByConditions(
                                        request.getTitle(),
                                        request.getContent(),
                                        request.toPageable()))
                        .willReturn(new PageImpl<>(list, request.toPageable(), list.size()));

                // when
                final ListResponse<SampleResponse> response = sampleService.search(request);

                // then
                assertSoftly(
                        it -> {
                            it.assertThat(response.getTotalCount()).isEqualTo(1);
                            it.assertThat(response.getData().getFirst().id()).isEqualTo(id);
                            it.assertThat(response.getData().getFirst().title()).isEqualTo(title);
                            it.assertThat(response.getData().getFirst().content())
                                    .isEqualTo(content);
                        });
            }
        }

        @Nested
        @DisplayName("샘플 데이터를 저장할 때,")
        class register {

            @Test
            @DisplayName("성공적으로 저장합니다.")
            void success() {
                // given
                final String title = "제목";
                final String content = "내용";
                final SampleSaveRequest request = SampleSaveRequestFixture.fixture(title, content);

                final Sample sample = SampleFixture.fixture(1L, title, content);

                given(sampleRepository.save(any(Sample.class))).willReturn(sample);

                // when & then
                assertDoesNotThrow(() -> sampleService.register(request));
            }
        }

        @Nested
        @DisplayName("샘플 데이터를 수정할 때,")
        class modify {

            @Test
            @DisplayName("데이터가 존재하지 않을 경우 오류가 발생합니다.")
            void isNull() {
                // given
                final Long id = 1L;
                final String title = "수정제목";
                final String content = "수정내용";
                final SampleUpdateRequest request =
                        SampleUpdateRequestFixture.fixture(title, content);

                given(sampleRepository.findById(id)).willReturn(Optional.empty());

                // when & then
                assertThatThrownBy(() -> sampleService.modify(id, request))
                        .isInstanceOf(BusinessException.class)
                        .hasMessage("존재하지 않는 데이터입니다.");
            }

            @Test
            @DisplayName("성공적으로 수정합니다.")
            void success() {
                // given
                final Long id = 1L;
                final String title = "수정제목";
                final String content = "수정내용";
                final SampleUpdateRequest request =
                        SampleUpdateRequestFixture.fixture(title, content);

                final Sample sample = spy(SampleFixture.fixture(1L, "제목", "내용"));

                given(sampleRepository.findById(id)).willReturn(Optional.of(sample));

                // when
                assertDoesNotThrow(() -> sampleService.modify(id, request));

                // then
                verify(sample).modify(title, content);
            }

            @Nested
            @DisplayName("샘플 데이터를 삭제할 때,")
            class remove {

                ArgumentCaptor<Sample> captor = ArgumentCaptor.forClass(Sample.class);

                @Test
                @DisplayName("데이터가 존재하지 않을 경우 오류가 발생합니다.")
                void isNull() {
                    // given
                    final Long id = 1L;

                    given(sampleRepository.findById(id)).willReturn(Optional.empty());

                    // when & then
                    assertThatThrownBy(() -> sampleService.remove(id))
                            .isInstanceOf(BusinessException.class)
                            .hasMessage("존재하지 않는 데이터입니다.");
                }

                @Test
                @DisplayName("성공적으로 삭제합니다.")
                void success() {
                    // given
                    final Long id = 1L;
                    final Sample sample = spy(SampleFixture.fixture(id, "제목", "내용"));

                    given(sampleRepository.findById(id)).willReturn(Optional.of(sample));

                    // when
                    assertDoesNotThrow(() -> sampleService.remove(id));

                    // then
                    then(sampleRepository).should().delete(captor.capture());

                    Sample captorValue = captor.getValue();
                    assertThat(captorValue.isUse()).isFalse();
                }
            }
        }
    }
}
