import ReactLoading from 'react-loading'
import React from "react";
import {Container} from "react-bootstrap";
export const Loading = () =>{
    return(
        <Container className='d-flex mt-5 justify-content-center'>
            <ReactLoading type={"bars"} color={'#5a92ed'}width={'10%'}/>
        </Container>
    )
}