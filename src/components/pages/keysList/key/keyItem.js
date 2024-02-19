import {ButtonDel} from './buttonDel'
import {ButtonGet} from './buttonGet'
import {Mark} from './mark'
import {useState} from "react";
import {AcceptedApplicationsModal} from "../../../snippets/AcceptedApplicationsModal";
import {Button, Col, Container, Row} from "react-bootstrap";
import {useMutation, useQuery, useQueryClient} from "react-query";
import ApplicationService from "../../../../services/ApplicationService";
import {Loading} from "../../../snippets/Loading";
import KeyService from "../../../../services/KeyService";

import('../../../../styles/keysItem.css')


export const KeysItem = ({id, build, room, location, owner}) => {

    const [acceptedApplicationsModal, setAcceptedApplicationsModal] = useState(false)
    const queryClient = useQueryClient()

    const {
        data,
        isLoading,
        error
    } = useQuery(['accepted applications', build, room], () => ApplicationService.getAcceptedApplications({
        buildId: build,
        roomId: room
    }), {
        refetchOnWindowFocus: false,
        keepPreviousData: true,
    })

    const mutation = useMutation(() => KeyService.takeKey(id, owner.client_id), {
        onSuccess() {
            alert(`Ключ успешно принят`)
            queryClient.invalidateQueries(['keys'])
        }
    })

    const handleTakeKey =() =>{
        mutation.mutate()
    }

    if (isLoading) {
        return (
            <></>
        )
    }
    return (
        <>
            {data.data.length !== 0 &&
                <AcceptedApplicationsModal key_id={id} data={data.data} build={build} room={room}
                                           show={acceptedApplicationsModal}
                                           onHide={() => setAcceptedApplicationsModal(false)}></AcceptedApplicationsModal>}

            <Container className='d-flex border rounded-3 pt-2 pb-2 mb-3'>
                <Row className='d-flex justify-content-between align-items-center w-100'>
                    <Col className='d-flex gap-3'>
                        <span className='key-number'>Здание <span className='fw-bold'>{build}</span></span>
                        <span className='key-number'>Аудитория <span className='fw-bold'>{room}</span></span>
                    </Col>
                    <Col>
                        <div className='d-flex text-start'>
                            <span className='location'>{location === 'IN_DEANERY' && 'В деканате'}</span>
                            <span className='location fw-bolder me-3'>{owner && owner.name}</span>
                            <span className='location text-muted'>{owner && owner.email}</span>
                        </div>
                    </Col>
                    <Col className='text-end'>
                        {(location === 'IN_DEANERY' && data.data.length !== 0) &&
                            <Button onClick={() => setAcceptedApplicationsModal(true)}
                                    className="btn rounded-5 get border-0 btn-success">
                                <span className=' buttonName'>Отдать</span>
                            </Button>
                        }
                        {(location === 'IN_HAND') &&
                            <Button onClick={handleTakeKey} className="btn rounded-5 delit border-0">
                                Забрать
                            </Button>
                        }
                    </Col>
                </Row>

            </Container>
        </>
    )
}