const api = 'https://test-soulcial.discoverse.club/'

export default {
    login: {
        login: `${api}pfp/api/admin/login/login`,
        checkSteamId: `${api}/pfp/api/admin/login/checkSteamId`

    },
    nft: {
        getNFTPage: `${api}pfp/api/admin/explor/getNFTPage`, //NFT paging
        getNFTDetail: `${api}pfp/api/admin/explor/getNFTDetail`, //NFT details
        getNFTLevel: `${api}pfp/api/admin/nft/getNFTLevel`, //NFT grade
        getMintPicture: `${api}pfp/api/admin/nft/getMintPicture`, //Get pictures of casting NFT (return 6 pictures)
        mintFreeNft: `${api}pfp/api/admin/nft/mintFreeNft`, //Mint
        collectNFT: `${api}pfp/api/admin/explor/collectNFT`, //Buy NFT
        listNFTApprove: `${api}pfp/api/admin/home/listNFTApprove`, //Buy NFT
        cancelListNFT: `${api}/pfp/api/admin/home/cancelListNFT`, //取消出售
        collectNFTOnline: `${api}/pfp/api/admin/explor/collectNFTOnline` //购买

    },
    infor: {
        getUserInfo: `${api}pfp/api/admin/home/getUserInfo`, //Get user information
        getMintedNFTPage: `${api}pfp/api/admin/home/getMintedNFTPage`, //My Mint list
        getCollectNFTPage: `${api}pfp/api/admin/home/getCollectNFTPage`, //I bought a list.
        setUserStreamId: `${api}pfp/api/admin/home/setUserStreamId`, //setUserStreamId
        updateUserScore: `${api}pfp/api/admin/home/updateUserScore`, //setUserStreamId
        followUser: `${api}pfp/api/admin/home/followUser`, //setUserStreamId
        getFollowers: `${api}pfp/api/admin/home/getFollowers`, //被关注列表
        getFollowing: `${api}pfp/api/admin/home/getFollowing`, //关注列表
    }
}