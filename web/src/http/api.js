const api = 'http://8.136.19.106:9003/'

export default {
    login: {
        login: `${api}pfp/api/admin/login/login`
    },
    nft: {
        getNFTPage: `${api}pfp/api/admin/explor/getNFTPage`, //NFT paging
        getNFTDetail: `${api}pfp/api/admin/explor/getNFTDetail`, //NFT details
        getNFTLevel: `${api}pfp/api/admin/nft/getNFTLevel`, //NFT grade
        getMintPicture: `${api}pfp/api/admin/nft/getMintPicture`, //Get pictures of casting NFT (return 6 pictures)
        mintFreeNft: `${api}pfp/api/admin/nft/mintFreeNft`, //Mint
        collectNFT: `${api}pfp/api/admin/explor/collectNFT` //Buy NFT
    },
    infor: {
        getUserInfo: `${api}pfp/api/admin/home/getUserInfo`, //Get user information
        getMintedNFTPage: `${api}pfp/api/admin/home/getMintedNFTPage`, //My Mint list
        getCollectNFTPage: `${api}/pfp/api/admin/home/getCollectNFTPage`, //I bought a list.
        setUserStreamId: `${api}pfp/api/admin/home/setUserStreamId`, //setUserStreamId
    }
}