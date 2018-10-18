package org.superbiz.moviefun.albums;

import org.apache.tika.Tika;
import org.apache.tika.io.IOUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.superbiz.moviefun.blobstore.Blob;
import org.superbiz.moviefun.blobstore.BlobStore;
import org.superbiz.moviefun.blobstore.FileStore;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@Controller
@RequestMapping("/albums")
public class AlbumsController {

    private final AlbumsBean albumsBean;
    private FileStore fileStore = new FileStore();
    private Tika tika = new Tika();

//    @Autowired
    BlobStore blobStore;

    public AlbumsController(AlbumsBean albumsBean, BlobStore blobStore) {
        this.albumsBean = albumsBean;
        this.blobStore = blobStore;
    }


    @GetMapping
    public String index(Map<String, Object> model) {
        model.put("albums", albumsBean.getAlbums());
        return "albums";
    }

    @GetMapping("/{albumId}")
    public String details(@PathVariable long albumId, Map<String, Object> model) {
        model.put("album", albumsBean.find(albumId));
        return "albumDetails";
    }

    @PostMapping("/{albumId}/cover")
    public String uploadCover(@PathVariable long albumId, @RequestParam("file") MultipartFile uploadedFile) throws IOException {
        saveUpload(format("covers/%d", albumId), uploadedFile);

        return format("redirect:/albums/%d", albumId);
    }

    @GetMapping("/{albumId}/cover")
    public HttpEntity<byte[]> getCover(@PathVariable long albumId) throws IOException {
        Optional<Blob> maybeBlob = blobStore.get(format("covers/%d", albumId));
        Blob blob = maybeBlob.orElseGet(this::defaultCover);

        byte[] bytes = IOUtils.toByteArray(blob.inputStream);
        return new HttpEntity<>(bytes, createImageHttpHeaders(bytes));
    }

    private void saveUpload(String name, MultipartFile uploadedFile) throws IOException {
        byte[] bytes = IOUtils.toByteArray(uploadedFile.getInputStream());
        Blob blob = new Blob(name, new ByteArrayInputStream(bytes), tika.detect(bytes));
        blobStore.put(blob);
    }

    private HttpHeaders createImageHttpHeaders(byte[] imageBytes) throws IOException {
        String contentType = new Tika().detect(imageBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setContentLength(imageBytes.length);
        return headers;
    }

    private Blob defaultCover() {
        return new Blob("default-cover", this.getClass().getClassLoader().getResourceAsStream("default-cover.jpg"), IMAGE_JPEG_VALUE);
    }

}
