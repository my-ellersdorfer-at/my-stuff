package at.steell.mystuff.application.web;

import at.steell.mystuff.domain.entity.Asset;
import at.steell.mystuff.domain.interactor.AssetInteractor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import static at.steell.mystuff.application.user.UserSessionFromSecurityContextHelper.fromSpringSecurityContext;

@RequestMapping("/api/assets")
@RestController
public class AssetController {
    private final AssetInteractor assetInteractor;

    public AssetController(final AssetInteractor theAssetInteractor) {
        assetInteractor = theAssetInteractor;
    }

    @GetMapping("/{id}")
    Asset find(@PathVariable("id") final String id) {
        return assetInteractor.find(id, fromSpringSecurityContext().userName());
    }

    public record ListOfAssets(Collection<Asset> assets) {
    }

    @GetMapping("/list")
    ListOfAssets list() {
        return new ListOfAssets(assetInteractor.listAssets(fromSpringSecurityContext().userName()));
    }

    @PutMapping("/create")
    String create() {
        return assetInteractor.createAsset(fromSpringSecurityContext().userName());
    }
}
