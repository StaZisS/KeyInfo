import {useState} from "react";
import {AcceptedApplicationsModal} from "../../../snippets/AcceptedApplicationsModal";
import {Button, Col, Container, Row} from "react-bootstrap";
import {useMutation, useQuery, useQueryClient} from "react-query";
import ApplicationService from "../../../../services/ApplicationService";
import KeyService from "../../../../services/KeyService";
import styles from "./style.module.css";
import {BiSolidHide} from "react-icons/bi";
import {PiLockSimpleBold, PiLockSimpleOpenBold} from "react-icons/pi";

import('../../../../styles/keysItem.css')


export const KeysItem = ({id, build, room, location, owner, isPrivate}) => {

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

    const mutationTake = useMutation(() => KeyService.takeKey(id, owner.client_id), {
        onSuccess() {
            queryClient.invalidateQueries(['keys'])
        }
    })

    const mutationHide = useMutation(() => KeyService.hideKey(id, !isPrivate), {
        onSuccess() {
            queryClient.invalidateQueries(['keys'])
        }
    })

    const handleTakeKey = () => {
        mutationTake.mutate()
    }
    const handleHideKey = () => {
        mutationHide.mutate()
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

            <Container
                className={(isPrivate === true) ? 'd-flex border border-black rounded-3 pt-2 pb-2 mb-3 opacity-25 bg-opacity-10' : 'd-flex border rounded-3 pt-2 pb-2 mb-3'}>
                <Row className='d-flex justify-content-between align-items-center w-100'>
                    <Col className='d-flex gap-3'>
                        <span className='key-number'>Корпус <span className='fw-bold'>{build}</span></span>
                        <span className='key-number'>Аудитория <span className='fw-bold'>{room}</span></span>
                    </Col>
                    <Col>
                        <div className={`d-flex text-start ${styles.location}`}>
                            <span className='location'>{location === 'IN_DEANERY' && 'В деканате'}</span>
                            <span className='location fw-bolder me-3'>{owner && owner.name}</span>
                            <span className='location text-muted'>{owner && owner.email}</span>
                        </div>
                    </Col>
                    <Col md={2} className={`text-center ${styles.button_wrapper}`}>
                        {(location === 'IN_DEANERY' && data.data.length !== 0) &&
                            <Button onClick={() => setAcceptedApplicationsModal(true)}
                                    className={`btn rounded-5 w-100 get border-0 btn-success ${styles.button}`}>
                                Выдать
                            </Button>
                        }
                        {
                            (location === 'IN_DEANERY' && isPrivate === false && data.data.length === 0) &&
                            <Button onClick={handleHideKey} title={'Скрыть ключ'}
                                    className={'bg-transparent border-0 rounded-0'}>
                                <PiLockSimpleBold className={'text-black'}/>
                            </Button>
                        }
                        {
                            (isPrivate === true) &&
                            <Button onClick={handleHideKey} title={isPrivate ? 'Скрыть ключ' : 'Открыть ключ'}
                                    className={'bg-transparent border-0 rounded-0'}>
                                <PiLockSimpleOpenBold className={'text-black opacity-100'}/>
                            </Button>
                        }
                        {(location === 'IN_HAND') &&
                            <Button onClick={handleTakeKey}
                                    className={`btn rounded-5 w-100 delit border-0 ${styles.button}`}>
                                Забрать
                            </Button>
                        }
                    </Col>
                </Row>

            </Container>
        </>
    )
}